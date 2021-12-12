/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.GUI;

import com.milkyway.DAO.DongSPDAO;
import com.milkyway.DAO.LoaiHangDAO;
import com.milkyway.DAO.TheThanhVienDAO;
import com.milkyway.DAO.ThongKeDAO;
import com.milkyway.Model.DongSP;
import com.milkyway.Model.LoaiHang;
import com.milkyway.Model.TheThanhVien;
import com.milkyway.Utils.MsgBox;
import com.milkyway.Utils.XCurrency;
import java.io.FileOutputStream;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author PC
 */
public class ThongKeJPanel extends javax.swing.JPanel {

    /**
     * Creates new form ThongKe
     */
    LoaiHangDAO loaiHangDAO = new LoaiHangDAO();
    DongSPDAO dongSPDAO = new DongSPDAO();
    ThongKeDAO thongKeDAO = new ThongKeDAO();
    TheThanhVienDAO theThanhVienDAO = new TheThanhVienDAO();

    public ThongKeJPanel() {
        initComponents();
        init();
    }

    private void init() {
        fillComboBoxLoaiHang();
        fillComboBoxDongSP();
        fillThongKeSoLuongSP();
        fillToTableThongKeSP();
        fillThongKeDoanhThu();
        fillTableThongKeDoanhThuTheoNam();
        fillTableThongKeDoanhThuTheoKhoangThoiGian();
        fillTheThanhVien();
        fillDiemThanhVien();
        tongSoTheHienTai();
        tongSoTheHetHan();
        tongSoTheSapHetHan();
        fillDangKyTrong1Thang();
    }

    private void fillComboBoxLoaiHang() {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cbbLoaiSP.getModel();
        comboBoxModel.removeAllElements();
        comboBoxModel.addElement("Chọn loại sản phẩm");
        List<LoaiHang> lst = loaiHangDAO.selectAll();
        for (LoaiHang lh : lst) {
            comboBoxModel.addElement(lh.getTenLoai());
        }
    }

    private void fillComboBoxDongSP() {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cbbDongSP.getModel();
        comboBoxModel.removeAllElements();
        comboBoxModel.addElement("Chọn dòng sản phẩm");
        List<DongSP> lst = dongSPDAO.selectByStatus(true);
        for (DongSP dsp : lst) {
            comboBoxModel.addElement(dsp.getTenDongSP());
        }
    }

    private void fillThongKeSoLuongSP() {
        Object[] obj = thongKeDAO.thongKeSoLuongSanPham();
        lblSoSPDangKD.setText(obj[0].toString());
        lblSoSPHetHang.setText(obj[1].toString());
        lblSoSPSapHetHan.setText(obj[2].toString());
        lblSoSanPhamSapHetHang.setText(obj[3].toString());
        lblSoSanPhamNgungKD.setText(obj[4].toString());
    }

    private void fillToTableThongKeSP() {
        DefaultTableModel tableModel = (DefaultTableModel) tblSanPham.getModel();
        tableModel.setRowCount(0);
        try {
            List<Object[]> lst;
            switch (cbbHinhThucThongKe.getSelectedIndex()) {
                case 0:
                    lst = thongKeDAO.thongKeSanPhamMuaNhieuNhat(cbbLoaiSP.getSelectedItem().toString(), cbbDongSP.getSelectedItem().toString());
                    break;
                case 1:
                    lst = thongKeDAO.thongKeSanPhamMuaItNhat(cbbLoaiSP.getSelectedItem().toString(), cbbDongSP.getSelectedItem().toString());
                    break;
                case 2:
                    lst = thongKeDAO.thongKeSanPhamDoanhThuNhieuNhat(cbbLoaiSP.getSelectedItem().toString(), cbbDongSP.getSelectedItem().toString());
                    break;
                case 3:
                    lst = thongKeDAO.thongKeSanPhamDoanhThuItNhat(cbbLoaiSP.getSelectedItem().toString(), cbbDongSP.getSelectedItem().toString());
                    break;
                default:
                    lst = null;
            }
            for (Object[] obj : lst) {
                tableModel.addRow(new Object[]{
                    obj[0], obj[1], obj[2], obj[3], obj[4], obj[5], obj[6], obj[7], obj[8], obj[9]
                });
            }
        } catch (Exception e) {
        }
    }

    private void fillThongKeDoanhThu() {
        Object[] obj = thongKeDAO.thongKeDoanhThu();
        for (Object o : obj) {
            if (o == null) {
                o = 0;
            }
        }
        double doanhThuCaNam = Double.parseDouble(obj[0].toString());
        double doanhThuHomNay = Double.parseDouble(obj[1] == null ? "0" : obj[1].toString());
        double doanhThu7NgayGanNhat = Double.parseDouble(obj[2].toString());
        double doanhThuThangNay = Double.parseDouble(obj[3].toString());
        lblDoanhThuCaNam.setText(XCurrency.toCurrency(doanhThuCaNam).substring(4));
        lblDoanhThuHomNay.setText(XCurrency.toCurrency(doanhThuHomNay).substring(4));
        lblDoanhThu7NgayGanNhat.setText(XCurrency.toCurrency(doanhThu7NgayGanNhat).substring(4));
        lblDoanhThuThangNay.setText(XCurrency.toCurrency(doanhThuThangNay).substring(4));
    }

    private void fillTableThongKeDoanhThuTheoNam() {
        DefaultTableModel tableModel = (DefaultTableModel) tblThongKeDoanhThuTheoNam.getModel();
        tableModel.setRowCount(0);
        try {
            Object[] obj = thongKeDAO.thongKeDoanhThuTheoNam();
            tableModel.addRow(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void fillTableThongKeDoanhThuTheoKhoangThoiGian() {
        DefaultTableModel tableModel = (DefaultTableModel) tblThongKeDoanhThuTheoKhoangThoiGian.getModel();
        tableModel.setRowCount(0);
        try {
            List<Object[]> lst = thongKeDAO.thongKeDoanhThuTheoKhoangThoiGian(txtNgayBD.getDate(), txtNgayKT.getDate());
            System.out.println(txtNgayBD.getDate());
            System.out.println(txtNgayKT.getDate());
            for (Object[] obj : lst) {
                tableModel.addRow(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillTheThanhVien() {
        DefaultTableModel model = (DefaultTableModel) tblSoLuongTheTV.getModel();
        model.setRowCount(0);
        List<Object[]> List = thongKeDAO.getSoLuongTheThanhVien();
        for (Object[] row : List) {
            model.addRow(row);
        }
    }

    private void fillDiemThanhVien() {
        DefaultTableModel model = (DefaultTableModel) tblDiemTheThanhVien.getModel();
        model.setRowCount(0);
        List<Object[]> list = thongKeDAO.ThongKeThanhVien();
        for (Object[] row : list) {
            model.addRow(row);
        }
    }

    private void tongSoTheHienTai() {
        int count = 0;
        List<TheThanhVien> list = theThanhVienDAO.selectTheTVConHan();
        for (TheThanhVien soluong : list) {
            count++;
        }
        lblTongSoTheHienCo.setText(String.valueOf(count));
    }

    private void tongSoTheHetHan() {
        int count = 0;
        List<TheThanhVien> list = theThanhVienDAO.selectTheTVHetHan();
        for (TheThanhVien soluong : list) {
            count++;
        }
        lblSoTheHetHan.setText(String.valueOf(count));
    }

    private void tongSoTheSapHetHan() {
        int count = 0;
        List<TheThanhVien> list = thongKeDAO.selectSapHetHan();
        for (TheThanhVien soluong : list) {
            count++;
        }
        lblSoTheSapHetHan.setText(String.valueOf(count));
    }

    private void fillDangKyTrong1Thang() {
        int count = 0;
        List<TheThanhVien> list = thongKeDAO.selectTheThanhVienTaoTrongThang();
        for (TheThanhVien soluong : list) {
            count++;
        }
        lblSoTheDangKyMoi.setText(String.valueOf(count));
    }

    private void exportToExcel(JTable tbl, String name) {
        try {
            DefaultTableModel tableModel = (DefaultTableModel) tbl.getModel();
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet spreadsheet = workbook.createSheet(tbl.getName());

            XSSFRow row = null;
            Cell cell = null;

            row = spreadsheet.createRow((short) 0);
            row.setHeight((short) 500);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(name);
            spreadsheet.addMergedRegion(new CellRangeAddress(0, 0, cell.getColumnIndex(), tableModel.getColumnCount() - 1));

            row = spreadsheet.createRow((short) 1);
            row.setHeight((short) 500);
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                cell = row.createCell(i, CellType.STRING);
                cell.setCellValue(tableModel.getColumnName(i));
            }

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                row = spreadsheet.createRow((short) 2 + i);
                row.setHeight((short) 400);
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    cell = row.createCell(j);
                    cell.setCellValue(tbl.getValueAt(i, j).toString());
                }
            }

            JFileChooser fileChooser = new JFileChooser("doc/ThongKe");
            fileChooser.setDialogTitle("Save as ...");
            FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Files", "xls", "xlsx");
            fileChooser.setFileFilter(extensionFilter);
            int path = fileChooser.showSaveDialog(null);
            if (path != JFileChooser.APPROVE_OPTION) {
                return;
            }
            FileOutputStream out = new FileOutputStream(fileChooser.getSelectedFile() + ".xlsx");
            workbook.write(out);
            out.close();
            MsgBox.alert(this, "Xuất file excel thành công");

        } catch (Exception e) {
            MsgBox.alert(this, "Xuất file excel thất bại");
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        lblSoSPDangKD = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        lblSoSPHetHang = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        lblSoSPSapHetHan = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        lblSoSanPhamSapHetHang = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        lblSoSanPhamNgungKD = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        cbbLoaiSP = new javax.swing.JComboBox<>();
        cbbDongSP = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cbbHinhThucThongKe = new javax.swing.JComboBox<>();
        srpSanPhamDangKD = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        btnExcelSP = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblThongKeDoanhThuTheoNam = new javax.swing.JTable();
        jPanel18 = new javax.swing.JPanel();
        lblDoanhThuCaNam = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        lblDoanhThuHomNay = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        lblDoanhThu7NgayGanNhat = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        lblDoanhThuThangNay = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblThongKeDoanhThuTheoKhoangThoiGian = new javax.swing.JTable();
        btnExcelDoanhThuTheoNam = new javax.swing.JButton();
        btnExcelDoanhThuTheoKhoangThoiGian = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtNgayBD = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        txtNgayKT = new com.toedter.calendar.JDateChooser();
        btnLoc = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        lblTongSoTheHienCo = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        lblSoTheDangKyMoi = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        lblSoTheHetHan = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        lblSoTheSapHetHan = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSoLuongTheTV = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblDiemTheThanhVien = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnExcelDiemTheTV = new javax.swing.JButton();
        btnExcelSoLuongTheTV = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setBackground(new java.awt.Color(107, 185, 240));
        setMaximumSize(new java.awt.Dimension(1153, 767));
        setPreferredSize(new java.awt.Dimension(1153, 767));

        jPanel1.setBackground(new java.awt.Color(107, 185, 240));

        jPanel4.setBackground(new java.awt.Color(0, 0, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        lblSoSPDangKD.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblSoSPDangKD.setForeground(new java.awt.Color(255, 255, 255));
        lblSoSPDangKD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSoSPDangKD.setText("100000");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Số sản phẩm đang kinh doanh");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSoSPDangKD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSoSPDangKD, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 102, 102));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Số sản phẩm hết hàng");

        lblSoSPHetHang.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblSoSPHetHang.setForeground(new java.awt.Color(255, 255, 255));
        lblSoSPHetHang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSoSPHetHang.setText("1000");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblSoSPHetHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSoSPHetHang, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 0, 0));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Số sản phẩm sắp hết hạn");

        lblSoSPSapHetHan.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblSoSPSapHetHan.setForeground(new java.awt.Color(255, 255, 255));
        lblSoSPSapHetHan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSoSPSapHetHan.setText("1000");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblSoSPSapHetHan, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSoSPSapHetHan, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(255, 153, 0));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Số sản phẩm sắp hết hàng");

        lblSoSanPhamSapHetHang.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblSoSanPhamSapHetHang.setForeground(new java.awt.Color(255, 255, 255));
        lblSoSanPhamSapHetHang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSoSanPhamSapHetHang.setText("1000");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblSoSanPhamSapHetHang, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSoSanPhamSapHetHang, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(204, 204, 204));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Số sản phẩm ngừng kinh doanh");

        lblSoSanPhamNgungKD.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblSoSanPhamNgungKD.setForeground(new java.awt.Color(255, 255, 255));
        lblSoSanPhamNgungKD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSoSanPhamNgungKD.setText("1000");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                    .addComponent(lblSoSanPhamNgungKD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSoSanPhamNgungKD, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(107, 185, 240));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Phân loại", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 15), new java.awt.Color(255, 255, 255))); // NOI18N

        cbbLoaiSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Loại sản phẩm" }));
        cbbLoaiSP.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbHinhThucThongKeItemStateChanged(evt);
            }
        });

        cbbDongSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dòng sản phẩm" }));
        cbbDongSP.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbHinhThucThongKeItemStateChanged(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Chọn hình thức thống kê");

        cbbHinhThucThongKe.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        cbbHinhThucThongKe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Top sản phẩm được mua nhiều nhất", "Top sản phẩm được mua ít nhất", "Top sản phẩm doanh thu nhiều nhất", "Top sản phẩm doanh thu ít nhất" }));
        cbbHinhThucThongKe.setToolTipText("");
        cbbHinhThucThongKe.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbHinhThucThongKeItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbbLoaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbbDongSP, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbbHinhThucThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbHinhThucThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbbLoaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbDongSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Loại hàng", "Dòng sản phẩm", "Ngày xuất kho", "Hạn sử dụng", "Số lượng tồn", "Đơn giá", "Số lượng bán", "Doanh thu"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSanPham.setName("Thống kê sản phẩm"); // NOI18N
        tblSanPham.setRowHeight(25);
        tblSanPham.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        srpSanPhamDangKD.setViewportView(tblSanPham);

        btnExcelSP.setBackground(new java.awt.Color(0, 204, 51));
        btnExcelSP.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnExcelSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/excel.png"))); // NOI18N
        btnExcelSP.setText("Export to Excel");
        btnExcelSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcelSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelSPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(srpSanPhamDangKD)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnExcelSP)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPanel4, jPanel6, jPanel7, jPanel9});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExcelSP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(srpSanPhamDangKD, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel6, jPanel7, jPanel9});

        jTabbedPane1.addTab("Sản phẩm", jPanel1);

        jPanel2.setBackground(new java.awt.Color(107, 185, 240));

        tblThongKeDoanhThuTheoNam.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tblThongKeDoanhThuTheoNam.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Năm", "Tổng doanh thu", "Tháng có doanh thu cao nhất", "Tháng có doanh thu thấp nhất", "Doanh thu trung bình tháng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblThongKeDoanhThuTheoNam.setName("Thống kê doanh thu"); // NOI18N
        tblThongKeDoanhThuTheoNam.setRowHeight(30);
        jScrollPane3.setViewportView(tblThongKeDoanhThuTheoNam);

        jPanel18.setBackground(new java.awt.Color(0, 0, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        lblDoanhThuCaNam.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblDoanhThuCaNam.setForeground(new java.awt.Color(255, 255, 255));
        lblDoanhThuCaNam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDoanhThuCaNam.setText("100,000,000,000");

        jLabel41.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("Doanh thu cả năm nay");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDoanhThuCaNam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDoanhThuCaNam, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel19.setBackground(new java.awt.Color(0, 204, 0));
        jPanel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel42.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("Doanh thu hôm nay");

        lblDoanhThuHomNay.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblDoanhThuHomNay.setForeground(new java.awt.Color(255, 255, 255));
        lblDoanhThuHomNay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDoanhThuHomNay.setText(" 100,000,000");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDoanhThuHomNay, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDoanhThuHomNay, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel20.setBackground(new java.awt.Color(255, 0, 0));
        jPanel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel43.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("Doanh thu 7 ngày gần nhất");

        lblDoanhThu7NgayGanNhat.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblDoanhThu7NgayGanNhat.setForeground(new java.awt.Color(255, 255, 255));
        lblDoanhThu7NgayGanNhat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDoanhThu7NgayGanNhat.setText("40,000,000,000");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDoanhThu7NgayGanNhat, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDoanhThu7NgayGanNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel21.setBackground(new java.awt.Color(255, 153, 0));
        jPanel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel45.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("Doanh thu tháng này");

        lblDoanhThuThangNay.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblDoanhThuThangNay.setForeground(new java.awt.Color(255, 255, 255));
        lblDoanhThuThangNay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDoanhThuThangNay.setText("10,000,000,000");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDoanhThuThangNay, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                    .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDoanhThuThangNay, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblThongKeDoanhThuTheoKhoangThoiGian.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tblThongKeDoanhThuTheoKhoangThoiGian.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Thời gian", "Doanh thu", "Số hóa đơn thanh toán"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblThongKeDoanhThuTheoKhoangThoiGian.setName("Thống kê doanh thu"); // NOI18N
        tblThongKeDoanhThuTheoKhoangThoiGian.setRowHeight(25);
        jScrollPane4.setViewportView(tblThongKeDoanhThuTheoKhoangThoiGian);

        btnExcelDoanhThuTheoNam.setBackground(new java.awt.Color(0, 204, 51));
        btnExcelDoanhThuTheoNam.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnExcelDoanhThuTheoNam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/excel.png"))); // NOI18N
        btnExcelDoanhThuTheoNam.setText("Export to Excel");
        btnExcelDoanhThuTheoNam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcelDoanhThuTheoNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelDoanhThuTheoNamActionPerformed(evt);
            }
        });

        btnExcelDoanhThuTheoKhoangThoiGian.setBackground(new java.awt.Color(0, 204, 51));
        btnExcelDoanhThuTheoKhoangThoiGian.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnExcelDoanhThuTheoKhoangThoiGian.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/excel.png"))); // NOI18N
        btnExcelDoanhThuTheoKhoangThoiGian.setText("Export to Excel");
        btnExcelDoanhThuTheoKhoangThoiGian.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcelDoanhThuTheoKhoangThoiGian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelDoanhThuTheoKhoangThoiGianActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Từ");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("đến");

        btnLoc.setBackground(new java.awt.Color(153, 255, 255));
        btnLoc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnLoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/filter.png"))); // NOI18N
        btnLoc.setText("Lọc");
        btnLoc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(txtNgayBD, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txtNgayKT, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnLoc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnExcelDoanhThuTheoKhoangThoiGian))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1408, Short.MAX_VALUE)
                            .addComponent(btnExcelDoanhThuTheoNam)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(97, 97, 97))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPanel18, jPanel19, jPanel20, jPanel21});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExcelDoanhThuTheoNam)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnExcelDoanhThuTheoKhoangThoiGian)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(btnLoc)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(txtNgayBD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(txtNgayKT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel18, jPanel19, jPanel20, jPanel21});

        jTabbedPane1.addTab("Doanh thu", jPanel2);

        jPanel3.setBackground(new java.awt.Color(107, 185, 240));

        jPanel14.setBackground(new java.awt.Color(0, 0, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        lblTongSoTheHienCo.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblTongSoTheHienCo.setForeground(new java.awt.Color(255, 255, 255));
        lblTongSoTheHienCo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTongSoTheHienCo.setText("100000");

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("Tổng số thẻ hiện có");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTongSoTheHienCo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTongSoTheHienCo, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel15.setBackground(new java.awt.Color(0, 204, 0));
        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("Số thẻ đăng ký mới theo tháng");

        lblSoTheDangKyMoi.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblSoTheDangKyMoi.setForeground(new java.awt.Color(255, 255, 255));
        lblSoTheDangKyMoi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSoTheDangKyMoi.setText("1000");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblSoTheDangKyMoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSoTheDangKyMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel16.setBackground(new java.awt.Color(255, 0, 0));
        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Số thẻ đã hết hạn");

        lblSoTheHetHan.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblSoTheHetHan.setForeground(new java.awt.Color(255, 255, 255));
        lblSoTheHetHan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSoTheHetHan.setText("1000");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblSoTheHetHan, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSoTheHetHan, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel17.setBackground(new java.awt.Color(255, 153, 0));
        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("Số thẻ sắp hết hạn");

        lblSoTheSapHetHan.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblSoTheSapHetHan.setForeground(new java.awt.Color(255, 255, 255));
        lblSoTheSapHetHan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSoTheSapHetHan.setText("1000");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblSoTheSapHetHan, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSoTheSapHetHan, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblSoLuongTheTV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Năm", "Số thẻ thành viên", "Đăng ký sớm nhất", "Đăng ký muộn nhất"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSoLuongTheTV.setName("Thống kê thẻ thành viên"); // NOI18N
        tblSoLuongTheTV.setRowHeight(25);
        jScrollPane2.setViewportView(tblSoLuongTheTV);

        tblDiemTheThanhVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã thẻ", "Họ tên", "Giới tính", "Ngày sinh", "Số điện thoại", "CMND", "Email", "Ngày tạo", "Ngày hết hạn", "Điểm"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDiemTheThanhVien.setName("Thống kê thẻ thành viên"); // NOI18N
        tblDiemTheThanhVien.setRowHeight(25);
        jScrollPane5.setViewportView(tblDiemTheThanhVien);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Điểm thành viên");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Số lượng thẻ thành viên");

        btnExcelDiemTheTV.setBackground(new java.awt.Color(0, 204, 51));
        btnExcelDiemTheTV.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnExcelDiemTheTV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/excel.png"))); // NOI18N
        btnExcelDiemTheTV.setText("Export to Excel");
        btnExcelDiemTheTV.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcelDiemTheTV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelDiemTheTVActionPerformed(evt);
            }
        });

        btnExcelSoLuongTheTV.setBackground(new java.awt.Color(0, 204, 51));
        btnExcelSoLuongTheTV.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnExcelSoLuongTheTV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/excel.png"))); // NOI18N
        btnExcelSoLuongTheTV.setText("Export to Excel");
        btnExcelSoLuongTheTV.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcelSoLuongTheTV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelSoLuongTheTVActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnExcelSoLuongTheTV))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnExcelDiemTheTV))
                    .addComponent(jSeparator4)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane5))
                .addContainerGap(384, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(btnExcelSoLuongTheTV))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 14, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnExcelDiemTheTV)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Thẻ thành viên", jPanel3);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Tổng hợp - Thống kê");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbbHinhThucThongKeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbHinhThucThongKeItemStateChanged
        fillToTableThongKeSP();
    }//GEN-LAST:event_cbbHinhThucThongKeItemStateChanged

    private void btnExcelSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelSPActionPerformed
        exportToExcel(tblSanPham, cbbHinhThucThongKe.getSelectedItem().toString());
    }//GEN-LAST:event_btnExcelSPActionPerformed

    private void btnExcelDoanhThuTheoNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelDoanhThuTheoNamActionPerformed
        exportToExcel(tblThongKeDoanhThuTheoNam, "Thống kê doanh thu theo năm");
    }//GEN-LAST:event_btnExcelDoanhThuTheoNamActionPerformed

    private void btnExcelDoanhThuTheoKhoangThoiGianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelDoanhThuTheoKhoangThoiGianActionPerformed
        exportToExcel(tblThongKeDoanhThuTheoKhoangThoiGian, "Thống kê doanh thu theo khoảng thời gian");
    }//GEN-LAST:event_btnExcelDoanhThuTheoKhoangThoiGianActionPerformed

    private void btnLocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocActionPerformed
        fillTableThongKeDoanhThuTheoKhoangThoiGian();
    }//GEN-LAST:event_btnLocActionPerformed

    private void btnExcelDiemTheTVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelDiemTheTVActionPerformed
        exportToExcel(tblDiemTheThanhVien, "Thống kê điểm thẻ thành viên");
    }//GEN-LAST:event_btnExcelDiemTheTVActionPerformed

    private void btnExcelSoLuongTheTVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelSoLuongTheTVActionPerformed
        exportToExcel(tblSoLuongTheTV, "Thống kê số lượng thẻ thành viên");
    }//GEN-LAST:event_btnExcelSoLuongTheTVActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcelDiemTheTV;
    private javax.swing.JButton btnExcelDoanhThuTheoKhoangThoiGian;
    private javax.swing.JButton btnExcelDoanhThuTheoNam;
    private javax.swing.JButton btnExcelSP;
    private javax.swing.JButton btnExcelSoLuongTheTV;
    private javax.swing.JButton btnLoc;
    private javax.swing.JComboBox<String> cbbDongSP;
    private javax.swing.JComboBox<String> cbbHinhThucThongKe;
    private javax.swing.JComboBox<String> cbbLoaiSP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblDoanhThu7NgayGanNhat;
    private javax.swing.JLabel lblDoanhThuCaNam;
    private javax.swing.JLabel lblDoanhThuHomNay;
    private javax.swing.JLabel lblDoanhThuThangNay;
    private javax.swing.JLabel lblSoSPDangKD;
    private javax.swing.JLabel lblSoSPHetHang;
    private javax.swing.JLabel lblSoSPSapHetHan;
    private javax.swing.JLabel lblSoSanPhamNgungKD;
    private javax.swing.JLabel lblSoSanPhamSapHetHang;
    private javax.swing.JLabel lblSoTheDangKyMoi;
    private javax.swing.JLabel lblSoTheHetHan;
    private javax.swing.JLabel lblSoTheSapHetHan;
    private javax.swing.JLabel lblTongSoTheHienCo;
    private javax.swing.JScrollPane srpSanPhamDangKD;
    private javax.swing.JTable tblDiemTheThanhVien;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTable tblSoLuongTheTV;
    private javax.swing.JTable tblThongKeDoanhThuTheoKhoangThoiGian;
    private javax.swing.JTable tblThongKeDoanhThuTheoNam;
    private com.toedter.calendar.JDateChooser txtNgayBD;
    private com.toedter.calendar.JDateChooser txtNgayKT;
    // End of variables declaration//GEN-END:variables
}
