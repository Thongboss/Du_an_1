/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.GUI;

import com.milkyway.DAO.BanHangDAO;
import com.milkyway.DAO.HinhThucThanhToanDAO;
import com.milkyway.Model.ChiTietHoaDon;
import com.milkyway.Model.HinhThucThanhToan;
import com.milkyway.Model.HoaDon;
import com.milkyway.Model.NhanVien;
import com.milkyway.Model.TheThanhVien;
import com.milkyway.Utils.Auth;
import com.milkyway.Utils.JThread;
import com.milkyway.Utils.MsgBox;
import com.milkyway.Utils.Validator;
import com.milkyway.Utils.XCurrency;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PC
 */
public class BanHangJPanel extends javax.swing.JPanel {

    /**
     * Creates new form BanHangJPanel
     */
    BanHangDAO banHangDAO = new BanHangDAO();
    String loaiHD[] = {"Chờ thanh toán", "Đang giao"};
    int maxindex;
    double tongTien = 0, tienCanTra;
    List<Object[]> listDS = banHangDAO.loadSanPham();
    List<Object[]> listSP1 = new ArrayList<>();
    List<Object[]> listGH = new ArrayList<>();
    List<Object[]> listT = new ArrayList<>();
    List<Object[]> listTrungGian = new ArrayList<>();
    List<TheThanhVien> listTTV = banHangDAO.loadListTTV();
    List<NhanVien> ListNV = banHangDAO.loadListNV();
    List<HinhThucThanhToan> listHTTT = banHangDAO.loadListHTTT();
    List<HoaDon> listHD = banHangDAO.loadListHD();
    Object[] gh, sp1, sp2, sp3, sp4;
    int index = 0;
    int sl1, sl2, sl3, sl4;
    HinhThucThanhToanDAO hinhThucThanhToanDAO = new HinhThucThanhToanDAO();

    public BanHangJPanel() {
        initComponents();
        init();
        for (int i = 0; i < loaiHD.length; i++) {
            cbbLoaiHD.addItem(loaiHD[i]);
        }
        listTrungGian = loadTrang(listDS, index);
        loadSP(listTrungGian);
    }

    private void init() {
        lblMaNV.setText(Auth.user.getMaNV());
        lblMaNVDH.setText(Auth.user.getMaNV());
        JThread.runDateTime(lblNgayTao);
        JThread.runDateTime(lblNgayTaoDH);
        lblDiachi.setText("Nam Từ Liêm, Hà Nội");
        loadComboBoxHinhThucThanhToan();
    }

    private void loadComboBoxHinhThucThanhToan() {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cbbHinhThucThanhToan.getModel();
        comboBoxModel.removeAllElements();
        try {
            List<HinhThucThanhToan> lst = hinhThucThanhToanDAO.selectAll();
            for (HinhThucThanhToan httt : lst) {
                comboBoxModel.addElement(httt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void timSP() {
        String tim = txtTimSP.getText();
        for (int i = 0; i < listDS.size(); i++) {
            Object[] sp = listDS.get(i);
            String dong = sp[1].toString();
            String ten = sp[2].toString();
            if (dong.contains(tim) || ten.contains(tim)) {
                listT.add(sp);
            }
        }
        if (listT.isEmpty()) {
            MsgBox.alert(this, "Không tồn tại sản phẩm bạn muốn tìm!");
            return;
        }
        clearSP();
        listTrungGian = loadTrang(listT, 0);
        loadSP(listTrungGian);
        listT.removeAll(listT);
    }

    private void taoHoaDon() {
        double thanhtien;
        for (int i = 0; i < listGH.size(); i++) {
            Object[] sp = listGH.get(i);
            int sl = Integer.parseInt(sp[7].toString());
            double gia = Double.parseDouble(sp[5].toString());
            thanhtien = sl * gia;
            tongTien += thanhtien;
        }
        String matvv = txtTheTV.getText();
        double giam = 0, phikhac, thue;
        for (int i = 0; i < listTTV.size(); i++) {
            if (listTTV.get(i).getMaTheTV().equals(matvv)) {
                giam = tongTien * 0.03;
            }
        }
        if (txtPhikhac.getText().isEmpty()) {
            phikhac = 0;
        } else {
            phikhac = Double.parseDouble(txtPhikhac.getText());
        }
        thue = tongTien * 0.01;
        tienCanTra = tongTien + thue + phikhac - giam;
        lblTongTien.setText(XCurrency.toCurrency(tongTien));
        lblGiamgia.setText(XCurrency.toCurrency(giam));
        lblTienCanTra.setText(XCurrency.toCurrency(tienCanTra));
    }

    void clearSP() {
        ImageIcon icon = new ImageIcon();
        lblAnh1.setIcon(icon);
        lblDongsp1.setText("");
        lblKL1.setText("");
        lblTensp1.setText("");
        lblDonGia.setText("");

        lblAnh2.setIcon(icon);
        lblDongsp2.setText("");
        lblKL2.setText("");
        lblTensp2.setText("");
        lblDongia1.setText("");

        lblAnh3.setIcon(icon);
        lblDongsp3.setText("");
        lblKL3.setText("");
        lblTensp3.setText("");
        lblDongia2.setText("");

        lblAnh4.setIcon(icon);
        lblDongsp4.setText("");
        lblKL4.setText("");
        lblTensp4.setText("");
        lblDongia3.setText("");
    }

    private List<Object[]> loadTrang(List<Object[]> list, int index) {
//        List<Object[]> list = banHangDAO.loadSanPham();
        int test = list.size() / 4;
        int du = list.size() % 4;

        if (du > 0) {
            maxindex = test + 1;
        }
        if (du == 0) {
            maxindex = test;
        }
        lblSP.setText(String.valueOf(index + 1) + "/" + String.valueOf(maxindex));
        int test1 = list.size();
        listSP1.removeAll(listSP1);
        int start = index * 4;
        int end = (index + 1) * 4;
        for (int i = start; i < end; i++) {
            if (i >= test1) {
                break;
            }
            listSP1.add(list.get(i));
        }
        return listSP1;
    }
    
    void thanhToan() {
        try {
            StringBuilder sb = new StringBuilder();
            Validator.checkMoney(txtTienkhachtra, sb);
            if (sb.length() > 0) {
                MsgBox.alert(this, sb.toString());
                return;
            }
            double tienKhachTra = Double.parseDouble(txtTienkhachtra.getText());
            double tienThua = tienKhachTra - tienCanTra;
            if (tienThua < 0) {
                MsgBox.alert(this, "Tiền thanh toán chưa đủ!");
                return;
            } else {
                lblTienThua.setText(String.valueOf(tienThua));
            }
            String manv = lblMaNV.getText();
            int idNV = 0;
            String maHD = listHD.get(listHD.size() - 1).getMaHD();
            int mahdd = Integer.parseInt(maHD.substring(2));
            mahdd++;
            String thetv = txtTheTV.getText();
            int idTTV = 0;
            for (int i = 0; i < listTTV.size(); i++) {
                if (listTTV.get(i).getMaTheTV().equals(thetv)) {
                    idTTV = listTTV.get(i).getIDTheTV();
                }
            }
            for (int i = 0; i < ListNV.size(); i++) {
                if (ListNV.get(i).getMaNV().equals(manv)) {
                    idNV = ListNV.get(i).getIDNhanVien();
                }
            }
            String httt = cbbHinhThucThanhToan.getSelectedItem().toString();
            int idht = 0;
            for (int i = 0; i < listHTTT.size(); i++) {
                if (listHTTT.get(i).getTenHinhThucThanhToan().equals(httt)) {
                    idht = listHTTT.get(i).getIDHinhThucThanhToan();
                }
            }
            String ghichu = txtGhichuHD.getText();
            String trangThai = ckbChoThanhToan.isSelected() ? "Chờ thanh toán" : "Đã thanh toán";
            HoaDon hd = new HoaDon();
            hd.setMaHD("HD" + String.valueOf(mahdd));
            hd.setIDNhanVien(idNV);
            hd.setIDHinhThucThanhToan(idht);
            hd.setGhiChu(ghichu);
            hd.setTongTien(tongTien);
            hd.setTrangThai(trangThai);

            if (!thetv.isEmpty()) {
                hd.setIDTheTV(idTTV);
            }
            
            banHangDAO.insert_HD(hd);
            List<HoaDon> list = banHangDAO.loadListHD();
            int idhd2 = list.get(list.size() - 1).getIDHoaDon();
            for (int i = 0; i < listGH.size(); i++) {
                Object[] sp = listGH.get(i);
                int idctsp = Integer.parseInt(sp[8].toString());
                int sl = Integer.parseInt(sp[7].toString());
                double dongia = Double.parseDouble(sp[5].toString());
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setIDHoaDon(idhd2);
                cthd.setIDChiTietSP(idctsp);
                cthd.setSoLuong(sl);
                cthd.setDonGia(dongia);
                banHangDAO.insert_CTHD(cthd);
            }
            MsgBox.alert(this, "Thanh toán thành công!");
            listGH.removeAll(listGH);
            loadGH();
            fillTableHD();
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Thanh toán thất bại");
            throw new RuntimeException(e);
        }
    }

    private void loadGH() {
        DefaultTableModel model = (DefaultTableModel) tblGiohang.getModel();
        model.setRowCount(0);
        for (Object[] ak : listGH) {
            double thanhtien = Integer.parseInt(ak[7].toString()) * Double.parseDouble(ak[5].toString());
            model.addRow(new Object[]{
                ak[0], ak[2], ak[7], ak[5].toString(), XCurrency.toCurrency(thanhtien)
            });
        }
    }

    private void loadSP(List<Object[]> list) {
//        List<Object[]> list = loadTrang(index);
        if (list.size() == 0) {
            JOptionPane.showMessageDialog(this, "Danh sách sản phẩm rỗng");
        } else if (list.size() == 1) {
            sp1 = list.get(0);
            String anh1 = "img\\SanPham/" + sp1[6].toString();
            ImageIcon icon1 = new ImageIcon(new ImageIcon(anh1).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
            lblAnh1.setIcon(icon1);
            lblDongsp1.setText(sp1[1].toString());
            lblTensp1.setText(sp1[2].toString() + " " + sp1[3].toString());
            lblKL1.setText(sp1[4].toString());
            lblDonGia.setText(XCurrency.toCurrency(Double.parseDouble(sp1[5].toString())));
        } else if (list.size() == 2) {
            sp1 = list.get(0);
            String anh1 = "img\\SanPham/" + sp1[6].toString();
            ImageIcon icon1 = new ImageIcon(new ImageIcon(anh1).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
            lblAnh1.setIcon(icon1);
            lblDongsp1.setText(sp1[1].toString());
            lblTensp1.setText(sp1[2].toString() + " " + sp1[3].toString());
            lblKL1.setText(sp1[4].toString());
            lblDonGia.setText(XCurrency.toCurrency(Double.parseDouble(sp1[5].toString())));
            sp2 = list.get(1);
            String anh2 = "SanPham/" + sp2[6].toString();
            ImageIcon icon2 = new ImageIcon(new ImageIcon(anh2).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
            lblAnh2.setIcon(icon2);
            lblDongsp2.setText(sp2[1].toString());
            lblTensp2.setText(sp2[2].toString() + " " + sp2[3].toString());
            lblKL2.setText(sp2[4].toString());
            lblDongia1.setText(XCurrency.toCurrency(Double.parseDouble(sp2[5].toString())));
        } else if (list.size() == 3) {
            sp1 = list.get(0);
            String anh1 = "img\\SanPham/" + sp1[6].toString();
            ImageIcon icon1 = new ImageIcon(new ImageIcon(anh1).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
            lblAnh1.setIcon(icon1);
            lblDongsp1.setText(sp1[1].toString());
            lblTensp1.setText(sp1[2].toString() + " " + sp1[3].toString());
            lblKL1.setText(sp1[4].toString());
            lblDonGia.setText(XCurrency.toCurrency(Double.parseDouble(sp1[5].toString())));
            String anh2 = "img\\SanPham/" + sp2[6].toString();
            ImageIcon icon2 = new ImageIcon(new ImageIcon(anh2).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
            lblAnh2.setIcon(icon2);
            lblDongsp2.setText(sp2[1].toString());
            lblTensp2.setText(sp2[2].toString() + " " + sp2[3].toString());
            lblKL2.setText(sp2[4].toString());
            lblDongia1.setText(XCurrency.toCurrency(Double.parseDouble(sp2[5].toString())));
            sp3 = list.get(2);
            String anh3 = "img\\SanPham/" + sp3[6].toString();
            ImageIcon icon3n = new ImageIcon(new ImageIcon(anh3).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
            lblAnh3.setIcon(icon3n);
            lblDongsp3.setText(sp3[1].toString());
            lblTensp3.setText(sp3[2].toString() + " " + sp3[3].toString());
            lblKL3.setText(sp3[4].toString());
            lblDongia2.setText(XCurrency.toCurrency(Double.parseDouble(sp3[5].toString())));
        } else {
            sp1 = list.get(0);
            String anh1 = "img\\SanPham/" + sp1[6].toString();
            ImageIcon icon1 = new ImageIcon(new ImageIcon(anh1).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
            lblAnh1.setIcon(icon1);
            lblDongsp1.setText(sp1[1].toString());
            lblTensp1.setText(sp1[2].toString() + " " + sp1[3].toString());
            lblKL1.setText(sp1[4].toString());
            lblDonGia.setText(XCurrency.toCurrency(Double.parseDouble(sp1[5].toString())));
            sp2 = list.get(1);
            String anh2 = "img\\SanPham/" + sp2[6].toString();
            ImageIcon icon2 = new ImageIcon(new ImageIcon(anh2).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
            lblAnh2.setIcon(icon2);
            lblDongsp2.setText(sp2[1].toString());
            lblTensp2.setText(sp2[2].toString() + " " + sp2[3].toString());
            lblKL2.setText(sp2[4].toString());
            lblDongia1.setText(XCurrency.toCurrency(Double.parseDouble(sp2[5].toString())));
            sp3 = list.get(2);
            String anh3 = "img\\SanPham/" + sp3[6].toString();
            ImageIcon icon3 = new ImageIcon(new ImageIcon(anh3).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
            lblAnh3.setIcon(icon3);
            lblDongsp3.setText(sp3[1].toString());
            lblTensp3.setText(sp3[2].toString() + " " + sp3[3].toString());
            lblKL3.setText(sp3[4].toString());
            lblDongia2.setText(XCurrency.toCurrency(Double.parseDouble(sp3[5].toString())));
            sp4 = list.get(3);
            String anh4 = "img\\SanPham/" + sp4[6].toString();
            ImageIcon icon4 = new ImageIcon(new ImageIcon(anh4).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
            lblAnh4.setIcon(icon4);
            lblDongsp4.setText(sp4[1].toString());
            lblTensp4.setText(sp4[2].toString() + " " + sp4[3].toString());
            lblKL4.setText(sp4[4].toString());
            lblDongia3.setText(XCurrency.toCurrency(Double.parseDouble(sp4[5].toString())));
        }
    }

    void fillTableHD() {
        String cbb = cbbLoaiHD.getSelectedItem().toString();
        DefaultTableModel model = (DefaultTableModel) tblDSHD.getModel();
        model.setRowCount(0);
        List<Object[]> list = banHangDAO.loadHoaDon(cbb);
        for (Object[] hd : list) {
            model.addRow(hd);
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

        jPanel13 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblMaNV = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtTheTV = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblDiachi = new javax.swing.JLabel();
        lblNgayTao = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblGiamgia = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtPhikhac = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        cbbHinhThucThanhToan = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        ckbChoThanhToan = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtGhichuHD = new javax.swing.JTextArea();
        lblTienCanTra = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtTienkhachtra = new javax.swing.JTextField();
        ckbNhanHD = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        lblThueVAT = new javax.swing.JLabel();
        lblTienThua = new javax.swing.JLabel();
        btnXuatHD = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        lblMaNVDH = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtMaTVDH = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        lblNgayTaoDH = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtNhanhang = new javax.swing.JTextArea();
        jPanel8 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        lblTienDH = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        lblGiamgiaDH = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtPhiDH = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        lblKhachcantraDH = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        cbbHinhthucThanhtoan = new javax.swing.JComboBox<>();
        jLabel36 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtGhichuDH = new javax.swing.JTextArea();
        btnNewdonDH = new javax.swing.JButton();
        btnAdddonDH = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGiohang = new javax.swing.JTable();
        btnBo = new javax.swing.JButton();
        ckbDatHang = new javax.swing.JCheckBox();
        btnTaoHD = new javax.swing.JButton();
        btnQuet = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblNextSP = new javax.swing.JButton();
        btnBackSP = new javax.swing.JButton();
        lblSP = new javax.swing.JLabel();
        txtTimSP = new javax.swing.JTextField();
        btnTimSP = new javax.swing.JButton();
        pnlDSSP = new javax.swing.JPanel();
        pnlSP1 = new javax.swing.JPanel();
        lblAnh1 = new javax.swing.JLabel();
        lblDongsp1 = new javax.swing.JLabel();
        lblTensp1 = new javax.swing.JLabel();
        lblKL1 = new javax.swing.JLabel();
        lblDonGia = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        pnlSP2 = new javax.swing.JPanel();
        lblAnh2 = new javax.swing.JLabel();
        lblDongsp2 = new javax.swing.JLabel();
        lblTensp2 = new javax.swing.JLabel();
        lblKL2 = new javax.swing.JLabel();
        lblDongia1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        pnlSP3 = new javax.swing.JPanel();
        lblAnh3 = new javax.swing.JLabel();
        lblDongsp3 = new javax.swing.JLabel();
        lblTensp3 = new javax.swing.JLabel();
        lblKL3 = new javax.swing.JLabel();
        lblDongia2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        pnlSP4 = new javax.swing.JPanel();
        lblAnh4 = new javax.swing.JLabel();
        lblDongsp4 = new javax.swing.JLabel();
        lblTensp4 = new javax.swing.JLabel();
        lblKL4 = new javax.swing.JLabel();
        lblDongia3 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        btnResest = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDSHD = new javax.swing.JTable();
        cbbLoaiHD = new javax.swing.JComboBox<>();
        jLabel37 = new javax.swing.JLabel();
        pnlWebcam = new javax.swing.JPanel();

        setBackground(new java.awt.Color(107, 185, 240));

        jPanel13.setBackground(new java.awt.Color(107, 185, 240));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tạo hóa đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jTabbedPane1.setBackground(new java.awt.Color(107, 185, 240));

        jPanel3.setBackground(new java.awt.Color(107, 185, 240));

        jPanel5.setBackground(new java.awt.Color(107, 185, 240));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin chung", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel3.setText("Mã Nhân viên");

        lblMaNV.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblMaNV.setForeground(new java.awt.Color(0, 0, 255));

        jLabel5.setText("Mã thẻ thành viên");

        jLabel6.setText("Ngày tạo");

        jLabel8.setText("Địa chỉ cửa hàng");

        lblDiachi.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblDiachi.setForeground(new java.awt.Color(0, 0, 255));

        lblNgayTao.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblNgayTao.setForeground(new java.awt.Color(0, 0, 255));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDiachi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTheTV, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                            .addComponent(lblNgayTao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(37, 37, 37)
                        .addComponent(lblMaNV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                    .addComponent(lblMaNV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(txtTheTV)))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNgayTao))
                .addGap(18, 18, 18)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDiachi, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblNgayTao, txtTheTV});

        jPanel6.setBackground(new java.awt.Color(107, 185, 240));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chi tiết", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel10.setText("Tổng tiền hàng");

        lblTongTien.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblTongTien.setForeground(new java.awt.Color(0, 0, 255));
        lblTongTien.setText("VND 100,000");

        jLabel12.setText("Giảm giá");

        lblGiamgia.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblGiamgia.setForeground(new java.awt.Color(255, 0, 0));
        lblGiamgia.setText("10%");

        jLabel14.setText("Phí khác");

        jLabel15.setText("Hình thức thanh toán");

        cbbHinhThucThanhToan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền mặt" }));

        jLabel16.setText("Khách cần trả");

        jLabel17.setText("Tiền trả lại");

        ckbChoThanhToan.setText("Chờ thanh toán");

        jLabel18.setText("Ghi chú hóa đơn");

        txtGhichuHD.setColumns(20);
        txtGhichuHD.setRows(5);
        jScrollPane4.setViewportView(txtGhichuHD);

        lblTienCanTra.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTienCanTra.setForeground(new java.awt.Color(0, 0, 255));
        lblTienCanTra.setText("VND 90,000");

        jLabel31.setText("Khách trả");

        txtTienkhachtra.setName("Tiền khách trả"); // NOI18N

        ckbNhanHD.setSelected(true);
        ckbNhanHD.setText("Nhận kèm hóa đơn");

        jLabel1.setText("Thuế VAT");

        lblThueVAT.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblThueVAT.setForeground(new java.awt.Color(255, 51, 51));
        lblThueVAT.setText("1%");

        lblTienThua.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTienThua.setForeground(new java.awt.Color(0, 0, 255));
        lblTienThua.setText("VND 10,000");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTongTien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblGiamgia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtPhikhac)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(lblThueVAT, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)
                                .addComponent(lblTienThua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(ckbChoThanhToan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ckbNhanHD)
                        .addGap(19, 19, 19))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblTienCanTra, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                            .addComponent(txtTienkhachtra))
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(43, 43, 43)
                        .addComponent(cbbHinhThucThanhToan, 0, 211, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(lblTongTien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(lblGiamgia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhikhac, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblThueVAT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbHinhThucThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTienCanTra, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTienkhachtra, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTienThua))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckbChoThanhToan)
                    .addComponent(ckbNhanHD))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblTienCanTra, lblTienThua});

        btnXuatHD.setBackground(new java.awt.Color(102, 255, 102));
        btnXuatHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Dollar.png"))); // NOI18N
        btnXuatHD.setText("Thanh toán");
        btnXuatHD.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXuatHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatHDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(153, 153, 153)
                        .addComponent(btnXuatHD)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnXuatHD, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Hóa đơn", jPanel3);

        jPanel4.setBackground(new java.awt.Color(107, 185, 240));

        jPanel7.setBackground(new java.awt.Color(107, 185, 240));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin chung", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel19.setText("Mã nhân viên");

        lblMaNVDH.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 0)));

        jLabel21.setText("Mã thẻ thành viên");

        jLabel22.setText("Số điện thoại khách");

        jLabel23.setText("Ngày tạo");

        lblNgayTaoDH.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51)));

        jLabel25.setText("Địa chỉ nhận");

        txtNhanhang.setColumns(20);
        txtNhanhang.setRows(5);
        jScrollPane5.setViewportView(txtNhanhang);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblMaNVDH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtMaTVDH, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                                    .addComponent(txtSDT)
                                    .addComponent(lblNgayTaoDH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(23, 23, 23))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMaNVDH, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMaTVDH, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(lblNgayTaoDH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(107, 185, 240));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chi tiết", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel26.setText("Tiền hàng");

        lblTienDH.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51)));

        jLabel28.setText("Giảm giá");

        lblGiamgiaDH.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51)));

        jLabel32.setText("Phí khác");

        jLabel33.setText("Khách cần trả");

        lblKhachcantraDH.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51)));

        jLabel35.setText("HÌnh thức thanh toán");

        cbbHinhthucThanhtoan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Thanh toán khi nhận hàng" }));

        jLabel36.setText("Ghi chú");

        txtGhichuDH.setColumns(20);
        txtGhichuDH.setRows(5);
        jScrollPane6.setViewportView(txtGhichuDH);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel28)
                                .addComponent(jLabel32))
                            .addGap(68, 68, 68)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblTienDH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblGiamgiaDH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtPhiDH, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                .addComponent(lblKhachcantraDH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addGap(18, 18, 18)
                        .addComponent(cbbHinhthucThanhtoan, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel33))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTienDH, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(lblGiamgiaDH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPhiDH, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblKhachcantraDH, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbHinhthucThanhtoan, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnNewdonDH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Add.png"))); // NOI18N
        btnNewdonDH.setText("Tạo mới");

        btnAdddonDH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Add to basket.png"))); // NOI18N
        btnAdddonDH.setText("Thêm hóa đơn");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnAdddonDH, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(btnNewdonDH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNewdonDH)
                    .addComponent(btnAdddonDH))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Đặt hàng", jPanel4);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        jPanel1.setBackground(new java.awt.Color(107, 185, 240));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Giỏ hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        tblGiohang.setAutoCreateRowSorter(true);
        tblGiohang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGiohang.setRowHeight(25);
        jScrollPane2.setViewportView(tblGiohang);

        btnBo.setBackground(new java.awt.Color(255, 102, 102));
        btnBo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnBo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Remove from basket.png"))); // NOI18N
        btnBo.setText("Bỏ khỏi giỏ hàng");
        btnBo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBoActionPerformed(evt);
            }
        });

        ckbDatHang.setText("Đặt hàng");

        btnTaoHD.setBackground(new java.awt.Color(102, 255, 102));
        btnTaoHD.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnTaoHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnTaoHD.setText("Tạo hóa đơn");
        btnTaoHD.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTaoHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHDActionPerformed(evt);
            }
        });

        btnQuet.setBackground(new java.awt.Color(255, 255, 255));
        btnQuet.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnQuet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/barcode.png"))); // NOI18N
        btnQuet.setText("Quét mã");
        btnQuet.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnQuet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(ckbDatHang)
                        .addGap(18, 18, 18)
                        .addComponent(btnTaoHD)
                        .addGap(18, 18, 18)
                        .addComponent(btnBo)
                        .addGap(18, 18, 18)
                        .addComponent(btnQuet, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBo)
                    .addComponent(btnTaoHD)
                    .addComponent(ckbDatHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnQuet))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnBo, btnQuet, btnTaoHD});

        jPanel2.setBackground(new java.awt.Color(107, 185, 240));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        lblNextSP.setText(">>");
        lblNextSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblNextSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblNextSPActionPerformed(evt);
            }
        });

        btnBackSP.setText("<<");
        btnBackSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBackSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackSPActionPerformed(evt);
            }
        });

        lblSP.setForeground(new java.awt.Color(255, 255, 255));
        lblSP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSP.setText("1/200");

        btnTimSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Search.png"))); // NOI18N
        btnTimSP.setText("Tìm kiếm");
        btnTimSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTimSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimSPActionPerformed(evt);
            }
        });

        pnlDSSP.setLayout(new java.awt.GridLayout(1, 4));

        pnlSP1.setBackground(new java.awt.Color(107, 185, 240));

        lblAnh1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblAnh1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        lblAnh1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblAnh1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnh1MouseClicked(evt);
            }
        });

        lblDongsp1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblDongsp1.setForeground(new java.awt.Color(51, 51, 255));
        lblDongsp1.setText("jLabel2");

        lblTensp1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblTensp1.setForeground(new java.awt.Color(51, 51, 255));
        lblTensp1.setText("jLabel1");

        lblKL1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblKL1.setForeground(new java.awt.Color(255, 0, 0));
        lblKL1.setText("jLabel4");

        lblDonGia.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblDonGia.setForeground(new java.awt.Color(255, 0, 0));
        lblDonGia.setText("jLabel7");

        jLabel4.setText("Đơn giá:");

        jLabel20.setText("Số lượng:");

        javax.swing.GroupLayout pnlSP1Layout = new javax.swing.GroupLayout(pnlSP1);
        pnlSP1.setLayout(pnlSP1Layout);
        pnlSP1Layout.setHorizontalGroup(
            pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSP1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDongsp1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTensp1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlSP1Layout.createSequentialGroup()
                        .addGroup(pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel20))
                        .addGap(18, 18, 18)
                        .addGroup(pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblKL1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDonGia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(pnlSP1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblAnh1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );
        pnlSP1Layout.setVerticalGroup(
            pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSP1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnh1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDongsp1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTensp1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKL1)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDonGia)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDSSP.add(pnlSP1);

        pnlSP2.setBackground(new java.awt.Color(107, 185, 240));

        lblAnh2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblAnh2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        lblAnh2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblAnh2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnh2MouseClicked(evt);
            }
        });

        lblDongsp2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblDongsp2.setForeground(new java.awt.Color(0, 0, 255));
        lblDongsp2.setText("jLabel2");

        lblTensp2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblTensp2.setForeground(new java.awt.Color(0, 0, 255));
        lblTensp2.setText("jLabel1");

        lblKL2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblKL2.setForeground(new java.awt.Color(255, 0, 0));
        lblKL2.setText("jLabel4");

        lblDongia1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblDongia1.setForeground(new java.awt.Color(255, 0, 0));
        lblDongia1.setText("jLabel7");

        jLabel7.setText("Đơn giá:");

        jLabel13.setText("Số lượng:");

        javax.swing.GroupLayout pnlSP2Layout = new javax.swing.GroupLayout(pnlSP2);
        pnlSP2.setLayout(pnlSP2Layout);
        pnlSP2Layout.setHorizontalGroup(
            pnlSP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSP2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDongsp2, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                    .addComponent(lblTensp2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlSP2Layout.createSequentialGroup()
                        .addGroup(pnlSP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlSP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblKL2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDongia1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlSP2Layout.createSequentialGroup()
                        .addComponent(lblAnh2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlSP2Layout.setVerticalGroup(
            pnlSP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSP2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnh2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDongsp2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTensp2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKL2)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDongia1)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDSSP.add(pnlSP2);

        pnlSP3.setBackground(new java.awt.Color(107, 185, 240));

        lblAnh3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblAnh3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        lblAnh3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblAnh3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnh3MouseClicked(evt);
            }
        });

        lblDongsp3.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblDongsp3.setForeground(new java.awt.Color(0, 0, 255));
        lblDongsp3.setText("jLabel2");

        lblTensp3.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblTensp3.setForeground(new java.awt.Color(0, 0, 255));
        lblTensp3.setText("jLabel1");

        lblKL3.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblKL3.setForeground(new java.awt.Color(255, 0, 0));
        lblKL3.setText("jLabel4");

        lblDongia2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblDongia2.setForeground(new java.awt.Color(255, 0, 0));
        lblDongia2.setText("jLabel7");

        jLabel9.setText("Đơn giá:");

        jLabel11.setText("Số lượng:");

        javax.swing.GroupLayout pnlSP3Layout = new javax.swing.GroupLayout(pnlSP3);
        pnlSP3.setLayout(pnlSP3Layout);
        pnlSP3Layout.setHorizontalGroup(
            pnlSP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSP3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDongsp3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTensp3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlSP3Layout.createSequentialGroup()
                        .addGroup(pnlSP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)
                        .addGroup(pnlSP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblKL3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDongia2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlSP3Layout.createSequentialGroup()
                        .addComponent(lblAnh3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 32, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlSP3Layout.setVerticalGroup(
            pnlSP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSP3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnh3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDongsp3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTensp3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKL3)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDongia2)
                    .addComponent(jLabel9))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDSSP.add(pnlSP3);

        pnlSP4.setBackground(new java.awt.Color(107, 185, 240));

        lblAnh4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblAnh4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        lblAnh4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblAnh4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnh4MouseClicked(evt);
            }
        });

        lblDongsp4.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblDongsp4.setForeground(new java.awt.Color(0, 0, 255));
        lblDongsp4.setText("jLabel2");

        lblTensp4.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblTensp4.setForeground(new java.awt.Color(0, 0, 255));
        lblTensp4.setText("jLabel1");

        lblKL4.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblKL4.setForeground(new java.awt.Color(255, 0, 0));
        lblKL4.setText("jLabel4");

        lblDongia3.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblDongia3.setForeground(new java.awt.Color(255, 0, 0));
        lblDongia3.setText("jLabel7");

        jLabel24.setText("Đơn giá:");

        jLabel27.setText("Số lượng:");

        javax.swing.GroupLayout pnlSP4Layout = new javax.swing.GroupLayout(pnlSP4);
        pnlSP4.setLayout(pnlSP4Layout);
        pnlSP4Layout.setHorizontalGroup(
            pnlSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSP4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDongsp4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTensp4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlSP4Layout.createSequentialGroup()
                        .addGroup(pnlSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSP4Layout.createSequentialGroup()
                                .addGroup(pnlSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel27))
                                .addGap(9, 9, 9)
                                .addGroup(pnlSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblKL4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblDongia3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)))
                            .addComponent(lblAnh4, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlSP4Layout.setVerticalGroup(
            pnlSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSP4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnh4, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDongsp4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTensp4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKL4)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDongia3)
                    .addComponent(jLabel24))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDSSP.add(pnlSP4);

        btnResest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Refresh.png"))); // NOI18N
        btnResest.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnResest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResestActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlDSSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtTimSP)
                        .addGap(18, 18, 18)
                        .addComponent(btnTimSP)
                        .addGap(102, 102, 102)
                        .addComponent(btnBackSP)
                        .addGap(18, 18, 18)
                        .addComponent(lblSP, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblNextSP)
                        .addGap(18, 18, 18)
                        .addComponent(btnResest, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlDSSP, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTimSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBackSP)
                        .addComponent(lblSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblNextSP))
                    .addComponent(txtTimSP)
                    .addComponent(btnResest))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(107, 185, 240));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách hóa đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        tblDSHD.setAutoCreateRowSorter(true);
        tblDSHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã hóa đơn", "Mã nhân viên", "Tên khách hàng", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDSHD.setRowHeight(25);
        tblDSHD.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblDSHD);

        cbbLoaiHD.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbLoaiHDItemStateChanged(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Loại hóa đơn");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel37)
                        .addGap(27, 27, 27)
                        .addComponent(cbbLoaiHD, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbbLoaiHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        pnlWebcam.setPreferredSize(new java.awt.Dimension(411, 200));

        javax.swing.GroupLayout pnlWebcamLayout = new javax.swing.GroupLayout(pnlWebcam);
        pnlWebcam.setLayout(pnlWebcamLayout);
        pnlWebcamLayout.setHorizontalGroup(
            pnlWebcamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 411, Short.MAX_VALUE)
        );
        pnlWebcamLayout.setVerticalGroup(
            pnlWebcamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlWebcam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlWebcam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbbLoaiHDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbLoaiHDItemStateChanged
        fillTableHD();
    }//GEN-LAST:event_cbbLoaiHDItemStateChanged

    private void lblNextSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblNextSPActionPerformed
        index++;
        if (index >= maxindex) {
            index = 0;
        }
        listTrungGian = loadTrang(listDS, index);
        loadSP(listTrungGian);
    }//GEN-LAST:event_lblNextSPActionPerformed

    private void btnBackSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackSPActionPerformed
        index--;
        if (index < 0) {
            index = maxindex - 1;
        }
        listTrungGian = loadTrang(listDS, index);
        loadSP(listTrungGian);
    }//GEN-LAST:event_btnBackSPActionPerformed

    private void lblAnh1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnh1MouseClicked
        sl1 = 0;
        if (listGH.size() != 0) {
            try {
                for (int i = 0; i < listGH.size(); i++) {
                    Object[] obj = listGH.get(i);
                    int a = Integer.parseInt(obj[8].toString());
                    int b = Integer.parseInt(sp1[8].toString());
                    System.out.println(b);
                    if (a == b) {
                        MsgBox.confirm(this, "Sản phẩm đã tồn tại trong giỏ hàng. Bạn có muốn thay đổi số lượng?");
                        sl1 = Integer.parseInt(MsgBox.prompt(this, "Nhập số lượng mua: "));
                        obj[7] = sl1;
                        loadGH();
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        sl1 = Integer.parseInt(MsgBox.prompt(this, "Nhập số lượng mua: "));
        int slt = Integer.parseInt(sp1[4].toString());
        lblKL1.setText(String.valueOf(slt - sl1));
        sp1[7] = sl1;
        listGH.add(sp1);
        loadGH();
    }//GEN-LAST:event_lblAnh1MouseClicked

    private void lblAnh2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnh2MouseClicked
        sl2 = 0;
        if (listGH.size() != 0) {
            try {
                for (int i = 0; i < listGH.size(); i++) {
                    Object[] obj = listGH.get(i);
                    int a = Integer.parseInt(obj[8].toString());
                    int b = Integer.parseInt(sp2[8].toString());
                    if (a == b) {
                        MsgBox.confirm(this, "Sản phẩm đã tồn tại trong giỏ hàng. Bạn có muốn thay đổi số lượng?");
                        sl2 = Integer.parseInt(MsgBox.prompt(this, "Nhập số lượng mua: "));
                        obj[7] = sl2;
                        loadGH();
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        sl2 = Integer.parseInt(MsgBox.prompt(this, "Nhập số lượng mua: "));
        int slt = Integer.parseInt(sp2[4].toString());
        lblKL2.setText(String.valueOf(slt - sl2));
        sp2[7] = sl2;
        listGH.add(sp2);
        loadGH();
    }//GEN-LAST:event_lblAnh2MouseClicked

    private void lblAnh3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnh3MouseClicked
        sl3 = 0;
        if (listGH.size() != 0) {
            try {
                for (int i = 0; i < listGH.size(); i++) {
                    Object[] obj = listGH.get(i);
                    int a = Integer.parseInt(obj[8].toString());
                    int b = Integer.parseInt(sp3[8].toString());
                    if (a == b) {
                        MsgBox.confirm(this, "Sản phẩm đã tồn tại trong giỏ hàng. Bạn có muốn thay đổi số lượng?");
                        sl3 = Integer.parseInt(MsgBox.prompt(this, "Nhập số lượng mua: "));
                        obj[7] = sl3;
                        loadGH();
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        sl3 = Integer.parseInt(MsgBox.prompt(this, "Nhập số lượng mua: "));
        int slt = Integer.parseInt(sp3[4].toString());
        lblKL3.setText(String.valueOf(slt - sl3));
        sp3[7] = sl3;
        listGH.add(sp3);
        loadGH();
    }//GEN-LAST:event_lblAnh3MouseClicked

    private void lblAnh4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnh4MouseClicked
        sl4 = 0;
        if (listGH.size() != 0) {
            try {
                for (int i = 0; i < listGH.size(); i++) {
                    Object[] obj = listGH.get(i);
                    int a = Integer.parseInt(obj[8].toString());
                    int b = Integer.parseInt(sp4[8].toString());
                    if (a == b) {
                        MsgBox.confirm(this, "Sản phẩm đã tồn tại trong giỏ hàng. Bạn có muốn thay đổi số lượng?");
                        sl4 = Integer.parseInt(MsgBox.prompt(this, "Nhập số lượng mua: "));
                        obj[7] = sl4;
                        loadGH();
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        sl4 = Integer.parseInt(MsgBox.prompt(this, "Nhập số lượng mua: "));
        int slt = Integer.parseInt(sp4[4].toString());
        lblKL4.setText(String.valueOf(slt - sl4));
        sp4[7] = sl4;
        listGH.add(sp4);
        loadGH();
    }//GEN-LAST:event_lblAnh4MouseClicked

    private void btnBoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBoActionPerformed
        int chon = tblGiohang.getSelectedRow();
        if (chon < 0) {
            MsgBox.alert(this, "Bạn chưa chọn mục để loại bỏ!");
            return;
        }
        listGH.remove(chon);
        loadGH();
    }//GEN-LAST:event_btnBoActionPerformed

    private void btnTimSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimSPActionPerformed
        timSP();
    }//GEN-LAST:event_btnTimSPActionPerformed

    private void btnResestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResestActionPerformed
        txtTimSP.setText("");
        listTrungGian = loadTrang(listDS, 0);
        loadSP(listTrungGian);
//        clearSP();
    }//GEN-LAST:event_btnResestActionPerformed

    private void btnTaoHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoHDActionPerformed
        taoHoaDon();
    }//GEN-LAST:event_btnTaoHDActionPerformed

    private void btnXuatHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatHDActionPerformed
        thanhToan();
    }//GEN-LAST:event_btnXuatHDActionPerformed

    private void btnQuetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuetActionPerformed
        new QuetMaVachJFrame().setVisible(true);
    }//GEN-LAST:event_btnQuetActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdddonDH;
    private javax.swing.JButton btnBackSP;
    private javax.swing.JButton btnBo;
    private javax.swing.JButton btnNewdonDH;
    private javax.swing.JButton btnQuet;
    private javax.swing.JButton btnResest;
    private javax.swing.JButton btnTaoHD;
    private javax.swing.JButton btnTimSP;
    private javax.swing.JButton btnXuatHD;
    private javax.swing.JComboBox<String> cbbHinhThucThanhToan;
    private javax.swing.JComboBox<String> cbbHinhthucThanhtoan;
    private javax.swing.JComboBox<String> cbbLoaiHD;
    private javax.swing.JCheckBox ckbChoThanhToan;
    private javax.swing.JCheckBox ckbDatHang;
    private javax.swing.JCheckBox ckbNhanHD;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAnh1;
    private javax.swing.JLabel lblAnh2;
    private javax.swing.JLabel lblAnh3;
    private javax.swing.JLabel lblAnh4;
    private javax.swing.JLabel lblDiachi;
    private javax.swing.JLabel lblDonGia;
    private javax.swing.JLabel lblDongia1;
    private javax.swing.JLabel lblDongia2;
    private javax.swing.JLabel lblDongia3;
    private javax.swing.JLabel lblDongsp1;
    private javax.swing.JLabel lblDongsp2;
    private javax.swing.JLabel lblDongsp3;
    private javax.swing.JLabel lblDongsp4;
    private javax.swing.JLabel lblGiamgia;
    private javax.swing.JLabel lblGiamgiaDH;
    private javax.swing.JLabel lblKL1;
    private javax.swing.JLabel lblKL2;
    private javax.swing.JLabel lblKL3;
    private javax.swing.JLabel lblKL4;
    private javax.swing.JLabel lblKhachcantraDH;
    private javax.swing.JLabel lblMaNV;
    private javax.swing.JLabel lblMaNVDH;
    private javax.swing.JButton lblNextSP;
    private javax.swing.JLabel lblNgayTao;
    private javax.swing.JLabel lblNgayTaoDH;
    private javax.swing.JLabel lblSP;
    private javax.swing.JLabel lblTensp1;
    private javax.swing.JLabel lblTensp2;
    private javax.swing.JLabel lblTensp3;
    private javax.swing.JLabel lblTensp4;
    private javax.swing.JLabel lblThueVAT;
    private javax.swing.JLabel lblTienCanTra;
    private javax.swing.JLabel lblTienDH;
    private javax.swing.JLabel lblTienThua;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JPanel pnlDSSP;
    private javax.swing.JPanel pnlSP1;
    private javax.swing.JPanel pnlSP2;
    private javax.swing.JPanel pnlSP3;
    private javax.swing.JPanel pnlSP4;
    private javax.swing.JPanel pnlWebcam;
    private javax.swing.JTable tblDSHD;
    private javax.swing.JTable tblGiohang;
    private javax.swing.JTextArea txtGhichuDH;
    private javax.swing.JTextArea txtGhichuHD;
    private javax.swing.JTextField txtMaTVDH;
    private javax.swing.JTextArea txtNhanhang;
    private javax.swing.JTextField txtPhiDH;
    private javax.swing.JTextField txtPhikhac;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTheTV;
    private javax.swing.JTextField txtTienkhachtra;
    private javax.swing.JTextField txtTimSP;
    // End of variables declaration//GEN-END:variables
}
