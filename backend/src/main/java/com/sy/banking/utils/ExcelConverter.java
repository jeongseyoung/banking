package com.sy.banking.utils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.sy.banking.domain.item.TransactionListItem;
import com.sy.banking.enumbox.TransferType;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelConverter {

    public static void exportTransactions(HttpServletResponse res, List<TransactionListItem> transactions) 
    throws IOException {
        res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        res.setHeader("Content-Disposition", "attachment; filename=TransactionHistory_" + LocalDate.now() + ".xlsx");
        try(SXSSFWorkbook wb = new SXSSFWorkbook(100)) {
            
            Sheet sheet = wb.createSheet("Transaction history");
            ((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();   

            CellStyle headerStyle = createHeaderStyle(wb);
            CellStyle numberStyle = createNumberStyle(wb);
            CellStyle dateStyle = createDateStyle(wb);

            createHeader(sheet, headerStyle);

            fillData(sheet, transactions, numberStyle, dateStyle);

            autoSizeColumns(sheet, 7);

            wb.write(res.getOutputStream());

            //log.info("")            
        } catch (IOException e) {
            log.error("엑셀 파일 생성 실패", e);
            throw e;
        }
    }

    private static CellStyle createHeaderStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();

        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        
        return style;
    }

    private static CellStyle createNumberStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        return style;
    }

    private static CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));
        style.setAlignment(HorizontalAlignment.CENTER);
        
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        return style;
    }

    private static void createHeader(Sheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0); 

        String[] headers = {
            "계좌 ID", "상대 계좌 ID", "거래 유형", "금액", "거래 후 잔액", "메모", "거래 일시"
        };

        for(int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private static void fillData(Sheet sheet, List<TransactionListItem> transactions, CellStyle numberStyle, CellStyle dateStyle) {
        int rowNum = 1;

        for(TransactionListItem transaction: transactions) {
            Row row = sheet.createRow(rowNum++);

            //계좌 ID
            row.createCell(0).setCellValue(transaction.getAccountId());

            //상대 계좌 ID
            Cell cell1 = row.createCell(1);
            if(!Objects.isNull(transaction.getCounterPartyAccountId())) {
                cell1.setCellValue(transaction.getCounterPartyAccountId());
            } else {
                cell1.setCellValue("-");
            }

            //거래 유형
            row.createCell(2).setCellValue(getTransactionTypeKorean(transaction.getType()));

            //금액
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(transaction.getAmount());
            cell3.setCellStyle(numberStyle);
            
            //거래 후 잔액
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(transaction.getBalanceAfter());
            cell4.setCellStyle(numberStyle);
            
            //메모
            row.createCell(5).setCellValue(
                transaction.getMemo() != null ? transaction.getMemo() : "-"
            );
            
            //거래 일시
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(transaction.getCreatedAt().toString());
            cell6.setCellStyle(dateStyle);
        }
    }

    private static void autoSizeColumns(Sheet sheet, int columnCount) {
        for(int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1024);
        }
    }

    private static String getTransactionTypeKorean(TransferType type) {

        return switch(type.name()) {
            case "DEPOSIT" -> "입금";
            case "WITHDRAWAL" -> "출금";
            case "TRANSFER_IN" -> "받은 송금";
            case "TRANSFER_OUT" -> "보낸 송금";
            case "INTEREST" -> "이자";
            default -> type.name();
        };
    }



}
