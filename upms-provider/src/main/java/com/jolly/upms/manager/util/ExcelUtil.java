package com.jolly.upms.manager.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author liuhuanhuan
 */
public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 导入Excel入口方法
     *
     * @return 解析后的object, 第一个：成功解析的对象，第二个：失败的对象
     */
    public static <T> List<List<T>> importExcel(String path, Class<T> model) {
        File file = new File(path);
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.error("异常信息",e);
            return new ArrayList<List<T>>();
        }
        return importExcel(fis, model);
    }

    /**
     * 导入Excel入口方法
     *
     * @param excelInputStream excel的输入流
     * @param model            结果映射的对象类型
     * @return 解析后的object, 第一个：成功解析的对象，第二个：失败的对象
     */
    public static <T> List<List<T>> importExcel(InputStream excelInputStream, Class<T> model) {

        List<List<T>> result = new ArrayList<List<T>>();
        List<T> failData = new ArrayList<T>();
        List<T> successData = new LinkedList<T>();
        // 创建 POI文件系统对象
        Workbook wb;
        try {
            wb = new HSSFWorkbook(excelInputStream);
        } catch (IOException e) {
            throw new RuntimeException("excel不存在");
        }
        // 检查excel是否合法
        check(wb, model);

        // 目前只支持模板只有一个sheet
        Sheet sheet = wb.getSheetAt(0);
        Row row;
        Cell cell;

        // 拿到表头对应的field(如：商品名=goodsName中的goodsName)
        List<String> headerField = analysisHeaderField(sheet.getRow(0));

        // 从第二行开始，第一行是表头，第二行开始是文件内容
        for (int r = 1, lent = sheet.getLastRowNum() + 1; r < lent; r++) {
            row = sheet.getRow(r);
            // 按照每行内容将之设置到对象中
            if (null != row) {
                T object;
                try {
                    object = model.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("不存在对应的数据库对象：" + model.getSimpleName());
                }
                int colNum = row.getLastCellNum();
                Boolean success = true;
                for (int c = 0; c < headerField.size(); c++) {
                    cell = row.getCell(c);
                    Object value = getCellValue(cell);
                    // 把对应的Excel单元格中的内容赋值到对象中
                    try {
                        setValueToBean(object, headerField.get(c), value);
                    } catch (Exception e) {
                        logger.error("异常信息", e);
                        success = false;
                        break;
                    }
                }
                // 如果成功全部转换，加入返回队列
                if (success) {
                    successData.add(object);
                } else {
                    // 加入错误数据队列
                    failData.add(object);
                }
            }
        }
        result.add(successData);
        result.add(failData);
        return result;
    }

    /**
     * 导入Excel入口方法
     *
     * @param excelInputStream excel的输入流
     * @param model            结果映射的对象类型
     * @return 解析后的object, 第一个：成功解析的对象，第二个：失败的对象
     */
    public static <T> List<List<T>> importExcelByCODOrder(InputStream excelInputStream, Class<T> model) {

        List<List<T>> result = new ArrayList<List<T>>();
        List<T> failData = new ArrayList<T>();
        List<T> successData = new LinkedList<T>();
        // 创建 POI文件系统对象
        Workbook wb;
        try {
            wb = new HSSFWorkbook(excelInputStream);
        } catch (IOException e) {
            throw new RuntimeException("excel不存在");
        }

        // 目前只支持模板只有一个sheet
        Sheet sheet = wb.getSheetAt(0);
        Row row;
        Cell cell;

        // 拿到表头对应的field(如：商品名=goodsName中的goodsName)
        List<String> headerField = analysisHeaderField(sheet.getRow(0));

        // 从第二行开始，第一行是表头，第二行开始是文件内容
        for (int r = 1, lent = sheet.getLastRowNum() + 1; r < lent; r++) {
            row = sheet.getRow(r);
            // 按照每行内容将之设置到对象中
            if (null != row) {
                T object;
                try {
                    object = model.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("不存在对应的数据库对象：" + model.getSimpleName());
                }
                int colNum = row.getLastCellNum();
                Boolean success = true;
                for (int c = 0; c < headerField.size(); c++) {
                    cell = row.getCell(c);
                    Object value = getCellValue(cell);

                    if(c>0 && value==null)value = 0D;

                    // 把对应的Excel单元格中的内容赋值到对象中
                    try {
                        setValueToBean(object, headerField.get(c), value);
                    } catch (Exception e) {
                        logger.error("异常信息", e);
                        success = false;
                        break;
                    }
                }
                // 如果成功全部转换，加入返回队列
                if (success) {
                    successData.add(object);
                } else {
                    // 加入错误数据队列
                    failData.add(object);
                }
            }
        }
        result.add(successData);
        result.add(failData);
        return result;
    }

    /**
     * 检查excel的格式是否合法，表头是否是（中文=field）格式，field在对应的object中是否存在get和set方法
     *
     * @param wb    操作excel的句柄
     * @param model 对应的object
     */
    private static <T> void check(Workbook wb, Class<T> model) {
        int numOfSheet = wb.getNumberOfSheets();
        if (numOfSheet <= 0) {
            throw new RuntimeException("不合法的excel内容");
        }
        List<String> headerFields = getHeaderField(wb.getSheetAt(0).getRow(0));
        for (String headerField : headerFields) {
            if(StringUtils.isBlank(headerField)){
                continue;
            }
            if (!headerField.contains("=")) {
                throw new RuntimeException("excel格式错误，检查您是否修改了表头内容，错误数据：" + headerField);
            } else {
                headerField = headerField.substring(headerField.indexOf("=") + 1);
                try {
                    Method method = model.getMethod(getterMethodName(headerField));
                    model.getMethod(setterMethodName(headerField), method.getReturnType());
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("excel格式错误，检查您是否修改了表头内容，错误数据：" + headerField);
                }
            }
        }
    }

    /**
     * 解析表头数据，拿到对应的Object 的Field数组
     *
     * @param row 表头的行
     * @return 表头的值
     */
    private static List<String> analysisHeaderField(Row row) {
        List<String> headerField = new ArrayList<String>();
        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (null == row.getCell(i)) {
                break;
            }
            String value = row.getCell(i).toString().trim();
            if (StringUtils.isNotBlank(value) && value.contains("=")) {
                value = value.substring(value.indexOf("=") + 1);
            }
            headerField.add(value);
        }
        return headerField;
    }

    /**
     * 拿到excel的第一行数据，也就是表头
     *
     * @param row 表头的行
     * @return 表头的值
     */
    private static List<String> getHeaderField(Row row) {
        List<String> headerField = new ArrayList<String>();
        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (null == row.getCell(i)) {
                break;
            }
            String value = row.getCell(i).toString().trim();
            headerField.add(value);
        }
        return headerField;
    }

    /**
     * 把单元格里面的值赋值到相应的对象中
     *
     * @param object       要设置值得对象
     * @param propertyName 表头
     * @param value        Excel单元格中的值
     * @throws Exception 所有的异常抛出
     */
    private static void setValueToBean(Object object, String propertyName, Object value) throws Exception {
        if (value == null || StringUtils.isEmpty(propertyName) || object == null)
            return;
        // 根据其getter方法来获取参数类型
        Method met = object.getClass().getMethod(getterMethodName(propertyName));
        Class type = met.getReturnType();
        String typeName = type.getSimpleName().toLowerCase();
        // 针对不同参数转换值
        String str = (value.toString()).trim();
        if (StringUtils.isNotBlank(str)) {
            if ("date".equals(typeName)) {
                value = (Date) value;
            } else if ("boolean".equals(typeName)) {
                value = (Boolean) value;
            } else if ("short".equals(typeName)) {
                value =  Double.valueOf(str).shortValue();
            } else if ("integer".equals(typeName)) {
                value = Double.valueOf(str).intValue();
            } else if ("long".equals(typeName)) {
                value = Double.valueOf(str).longValue();
            } else if ("double".equals(typeName)) {
                value = Double.valueOf(str);
            } else if ("string".equals(typeName)) {
                value = str;
            } else if ("bigdecimal".equals(typeName)) {
                value = new BigDecimal(str);
            } else if("byte".equals(typeName)){
                value = Double.valueOf(str).byteValue();
            }
            // 通过setter方法设置到对象中
            met = object.getClass().getMethod(setterMethodName(propertyName), type);
            met.invoke(object, value);
        }
    }

    public static Object getValueFromBean(Object object, String propertyName) throws Exception {
        if (StringUtils.isEmpty(propertyName) || object == null) {
            return null;
        }
        // 根据其getter方法来获取参数类型
        Method met = object.getClass().getMethod(getterMethodName(propertyName));

        return met.invoke(object);
    }

    /**
     * 获得Excel单元格内容
     *
     * @param cell 列
     * @return 列值
     */
    private static Object getCellValue(Cell cell) {
        Object object = null;
        if (null == cell) {
            return null;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: // 数字
                if (DateUtil.isCellDateFormatted(cell)) {// 日期
                    double d = cell.getNumericCellValue();
                    object = DateUtil.getJavaDate(d);
                } else {
                    object = cell.getNumericCellValue();
                }
                break;
            case Cell.CELL_TYPE_STRING: // 字符串
                object = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN: // Boolean
                object = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_FORMULA: // 公式
                try {
                    object = String.valueOf(cell.getNumericCellValue());
                } catch (IllegalStateException e) {
                    object = String.valueOf(cell.getRichStringCellValue());
                }
                break;
            case Cell.CELL_TYPE_BLANK: // 空值
                object = "";
                break;
            case Cell.CELL_TYPE_ERROR: // 故障
                object = "";
                break;
        }

        return object;
    }

    /**
     * 根据属性名字获得对应的bean对象的getter名字
     *
     * @param beanPropertyName bean对象的属性名字
     * @return get方法名
     */
    private static String getterMethodName(String beanPropertyName) {
        return "get" + beanPropertyName.substring(0, 1).toUpperCase() + beanPropertyName.substring(1);
    }

    /**
     * 根据属性名字获得对应的bean对象的setter名字
     *
     * @param beanPropertyName bean对象的属性名字
     * @return set方法名
     */
    private static String setterMethodName(String beanPropertyName) {
        return "set" + beanPropertyName.substring(0, 1).toUpperCase() + beanPropertyName.substring(1);
    }

    /**
     * 导出excel
     *
     * @param fileName     文件名
     * @param otherInfos   要显示在表最上方的内容
     * @param column2Field 列名称，格式（中文=字段名，如商品名称=goodsName）
     * @param objectList   要导出的对象
     * @param isTemplate   是否是模板文件
     * @return 生成的excel文件
     */
    public static <T> File exportExcel(String fileName, List<List<String>> otherInfos, List<String> column2Field,
                                       List<T> objectList, Boolean isTemplate) {
        // 创建新的Excel 工作簿
        HSSFWorkbook workbook = mappingToExcel(otherInfos, column2Field, objectList, isTemplate);
        ExcelUtil.autoFitContent(workbook);
        return writeIntoFile(workbook, fileName);
    }

    /**
     * 将给定信息映射到excel的操作句柄中
     *
     * @param otherInfos   要显示在表最上方的内容
     * @param column2Field 列名称，格式（中文=字段名，如商品名称=goodsName）
     * @param objectList   要导出的对象
     * @param isTemplate   是否是模板文件
     * @return
     */
    public static <T> HSSFWorkbook mappingToExcel(List<List<String>> otherInfos, List<String> column2Field,
                                                  List<T> objectList, Boolean isTemplate) {
        // 创建新的Excel 工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 在Excel工作簿中建一工作表，其名为缺省值
        HSSFSheet sheet = workbook.createSheet();
        initCellStyle = initFont(workbook);
        // 标示行号
        int rowNum = 0;
        // 将要显示在表头的数据先写入
        if (CollectionUtils.isNotEmpty(otherInfos)) {
            for (rowNum = 0; rowNum < otherInfos.size(); rowNum++) {
                if (CollectionUtils.isEmpty(otherInfos.get(rowNum)))
                    continue;
                ExcelUtil.addRow(sheet, rowNum, otherInfos.get(rowNum));
            }
        }

        // 获取表头
        List<String> columnNames = ExcelUtil.getColumnName(column2Field, isTemplate);
        // 写入表头
        ExcelUtil.addRow(sheet, rowNum++, columnNames);

        // 写入内容
        if (CollectionUtils.isNotEmpty(objectList)) {
            List<String> fieldNames = ExcelUtil.getFieldName(column2Field);
            for (T object : objectList) {
                ExcelUtil.addRow(sheet, rowNum++, fieldNames, object);
            }
        }
        // 单元格根据内容进行自适应
        ExcelUtil.autoFitContent(workbook);
        return workbook;
    }

    private static HSSFCellStyle initCellStyle;

    /**
     * 初始化字体列宽等格式
     *
     * @param workbook
     * @return
     */
    public static HSSFCellStyle initFont(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12); // 字体高度
        font.setFontName("宋体"); // 字体
        // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度

        // 设置单元格格式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    /**
     * 写入文件
     *
     * @param workbook 操作句柄
     * @param fileName 文件名
     * @return 生成的文件
     */
    public static File writeIntoFile(HSSFWorkbook workbook, String fileName) {
        File file = new File(fileName + ".xls");
        // 新建一输出文件流
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            workbook.write(fOut);
        } catch (FileNotFoundException e) {
            logger.error("导出excel失败，找不到文件");
            throw new RuntimeException("导出excel失败，找不到文件");
        } catch (IOException ex) {
            throw new RuntimeException("导出excel失败，未知错误");
        } finally {
            if (fOut != null) {
                try {
                    fOut.flush();
                } catch (IOException e) {
                    logger.error("异常信息", e);
                }
            }
            // 操作结束，关闭文件
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    logger.error("异常信息", e);
                }
            }
        }
        return file;
    }

    /**
     * 增加一行
     *
     * @param sheet      工作表句柄
     * @param rowNum     行号
     * @param fieldNames 和对象属性对应的属性名，这里用来标示取值顺序
     * @param object     映射对象
     */
    public static <T> void addRow(HSSFSheet sheet, int rowNum, List<String> fieldNames, T object) {
        // 创建行
        HSSFRow row = sheet.createRow(rowNum);
        for (int cellNum = 0; cellNum < fieldNames.size(); cellNum++) {
            Object propertyValue = null;
            try {
                propertyValue = getValueFromBean(object, fieldNames.get(cellNum));
            } catch (Exception e) {
                propertyValue = "";
            }
            if (propertyValue == null) {
                continue;
            }
            ExcelUtil.addCell(row, cellNum, propertyValue);
        }
    }

    /**
     * 添加一个单元格
     *
     * @param row
     * @param cellNum
     * @param value
     */
    private static void addCell(HSSFRow row, Integer cellNum, Object value) {
        HSSFCell cell = row.createCell(cellNum);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellStyle(initCellStyle);
        // 在单元格中输入一些内容
        cell.setCellValue(value.toString());
    }

    /**
     * 增加一行
     *
     * @param sheet        工作表句柄
     * @param rowNum       行号
     * @param columnValues 列值
     */
    public static void addRow(HSSFSheet sheet, int rowNum, List<String> columnValues) {
        HSSFRow row = sheet.createRow(rowNum);
        for (int cellNum = 0; cellNum < columnValues.size(); cellNum++) {
            ExcelUtil.addCell(row, cellNum, columnValues.get(cellNum));
        }
    }

    /**
     * 为特定的某行某列设置值
     *
     * @param sheet   工作单元
     * @param rowNum  行号
     * @param cellNum 列号
     * @param value   值
     */
    public static void addCell(HSSFSheet sheet, int rowNum, int cellNum, String value) {
        HSSFRow row = sheet.getRow(rowNum);
        ExcelUtil.addCell(row, cellNum, value);
    }

    /**
     * 使单元格根据内容自适应
     *
     * @param workbook
     */
    public static void autoFitContent(HSSFWorkbook workbook) {
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            HSSFSheet sheet = workbook.getSheetAt(sheetNum);
            for (int columnNum = 0; columnNum < sheet.getRow(sheet.getLastRowNum()).getLastCellNum(); columnNum++) {
                sheet.autoSizeColumn(columnNum, true);
            }
        }
    }

    /**
     * 拿到属性名列表
     *
     * @param column2FieldList 表头和属性名映射字符串，用‘=’隔开
     * @return
     */
    private static List<String> getFieldName(List<String> column2FieldList) {
        List<String> fieldNames = new ArrayList<String>();
        for (String column2Field : column2FieldList) {
            if (column2Field.contains("=")) {
                String fieldName = column2Field.substring(column2Field.indexOf("=") + 1);
                fieldNames.add(fieldName);
            }
        }
        return fieldNames;
    }

    /**
     * 拿到表头列表
     *
     * @param column2FieldList 表头和属性名映射字符串，用‘=’隔开
     * @param isTemplate       是否是模板
     * @return
     */
    private static List<String> getColumnName(List<String> column2FieldList, Boolean isTemplate) {
        if (isTemplate) {
            return column2FieldList;
        }
        List<String> columnNames = new ArrayList<String>();
        for (String column2Field : column2FieldList) {
            if (column2Field.contains("=")) {
                String columnName = column2Field.substring(0, column2Field.indexOf("="));
                columnNames.add(columnName);
            } else {
                columnNames.add(column2Field);
            }
        }
        return columnNames;
    }

    /**
     * 校验文件类型
     *
     * @param excelFile excel文件
     * @return boolean true不合法
     */
    public static boolean checkFileLegal(MultipartFile excelFile) {

        String fileName = excelFile.getOriginalFilename();
        //如果不是excel文件，则直接返回
        return fileName == null || (!"xls".equals(fileName.substring(fileName.lastIndexOf(".") + 1)));
    }

}