/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.GUI;

import com.milkyway.DAO.BanHangDAO;
import com.milkyway.DAO.HinhThucThanhToanDAO;
import com.milkyway.DAO.SanPhamDAO;
import com.milkyway.DAO.TheThanhVienDAO;
import com.milkyway.Model.ChiTietComboSP;
import com.milkyway.Model.ChiTietDatHang;
import com.milkyway.Model.ChiTietHoaDon;
import com.milkyway.Model.ChiTietSanPham;
import com.milkyway.Model.ComBoSP;
import com.milkyway.Model.DatHang;
import com.milkyway.Model.HinhThucThanhToan;
import com.milkyway.Model.HoaDon;
import com.milkyway.Model.NhanVien;
import com.milkyway.Model.TheThanhVien;
import com.milkyway.Utils.Auth;
import com.milkyway.Utils.JThread;
import com.milkyway.Utils.*;
import com.milkyway.Utils.XCurrency;
import java.awt.Image;
import static java.sql.Types.NULL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PC
 */
public class BanHangJPanel extends javax.swing.JPanel {

    /**
     * Creates new form BanHangJPanel
     */
    BanHangDAO bh = new BanHangDAO();
    String loaiHD[] = {"Chờ thanh toán", "Đang giao"};
    String loadTT[] = {"Đã thanh toán", "Hủy đơn"};
    int maxIndex, hoaDon, soLuongTon, capNhat, thayDoi;
    double tongtien = 0, tienCanTra;
    List<Object[]> listDS = bh.loadSanPham();
    List<Object[]> listDSCB = bh.loadComBo();
    List<Object[]> listSP1 = new ArrayList<>();
    List<Object[]> listGH = new ArrayList<>();
    List<Object[]> listGH1 = new ArrayList<>();
    List<Object[]> listT = new ArrayList<>();
    List<Object[]> listTrungGian = new ArrayList<>();
    List<TheThanhVien> listTTV = bh.loadListTTV();
    List<NhanVien> ListNV = bh.loadListNV();
    List<ComBoSP> ListCBSP = bh.loadListComBoSP();
    List<ChiTietComboSP> ListCTCB = bh.loadListCTCB();
    List<HinhThucThanhToan> listHTTT = bh.loadListHTTT();
    List<HoaDon> listHD = bh.loadListHD();
    List<ChiTietSanPham> listCTSP = bh.loadListCTSP();
    List<ChiTietHoaDon> listCTHD = bh.loadListCTHD();
    List<DatHang> listDH = bh.loadListDH();
    List<ChiTietDatHang> listCTDH = bh.loadCTDH();
    Object[] gh, sp1, sp2, sp3, sp4;
    int index = 0, click;
    int sl1, sl2, sl3, sl4;
    double giamGia = 0, phiKhac = 0, thue = 0;
    SanPhamDAO sanPhamDAO = new SanPhamDAO();
    HinhThucThanhToanDAO hinhThucThanhToanDAO = new HinhThucThanhToanDAO();
    TheThanhVienDAO theThanhVienDAO = new TheThanhVienDAO();
    static Object[] sanPhamQuetTuBarcode = null;
    static boolean getSanPhamQuetTuBarcode = false;

    public BanHangJPanel() {
        initComponents();
        init();
        check_HD();
        for (int i = 0; i < loaiHD.length; i++) {
            cbbLoaiHD.addItem(loaiHD[i]);
        }
        for (int i = 0; i < loadTT.length; i++) {
            cbbUpdateTT.addItem(loadTT[i]);
        }
        for (int i = 0; i < listDS.size(); i++) {
            listTrungGian.add(listDS.get(i));
        }
        for (int i = 0; i < listDSCB.size(); i++) {
            listTrungGian.add(listDSCB.get(i));
        }
//        System.out.println(listTrungGian.size());
        loadSP(loadTrang(listTrungGian, index));
        txtPhikhac.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                taoHDTQ();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                taoHDTQ();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                taoHDTQ();
            }
        });
        txtPhiDH.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                taoHDDH();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                taoHDDH();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                taoHDDH();
            }
        });
    }

    private void init() {
        lblMaNV.setText(Auth.user.getMaNV());
        lblMaNVDH.setText(Auth.user.getMaNV());
        JThread.runDateTime(lblNgayTao);
        JThread.runDateTime(lblNgayTaoDH);
        loadComboBoxHinhThucThanhToan();
        loadComboBoxHinhThucThanhToanDH();
    }

    private void loadComboBoxHinhThucThanhToan() {
        try {
            DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cbbHinhThucThanhToan.getModel();
            comboBoxModel.removeAllElements();
            List<HinhThucThanhToan> lst = hinhThucThanhToanDAO.selectAll();
            for (HinhThucThanhToan httt : lst) {
                comboBoxModel.addElement(httt);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    void clearDH() {
        txtMaTVDH.setText("");
        txtHoTenDH.setText("");
        txtDiemTVDH.setText("");
        txtSDT.setText("");
        txtNhanhang.setText("");
        lblTienDH.setText("");
        lblGiamgiaDH.setText("");
        txtPhiDH.setText("");
        txtGhichuDH.setText("");
        lblKhachcantraDH.setText("");
        cbbHinhthucThanhtoanDH.setSelectedIndex(0);
    }

    private void loadComboBoxHinhThucThanhToanDH() {
        try {
            DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cbbHinhthucThanhtoanDH.getModel();
            comboBoxModel.removeAllElements();
            List<HinhThucThanhToan> lst = hinhThucThanhToanDAO.selectAll();
            for (HinhThucThanhToan httt : lst) {
                comboBoxModel.addElement(httt);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    void timSP() {
        try {
            String tim = txtTimSP.getText();
            for (int i = 0; i < listTrungGian.size(); i++) {
                Object[] sp = listTrungGian.get(i);
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
            loadSP(loadTrang(listT, 0));
            listT.removeAll(listT);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    void taoHDDH() {
        if (listGH.size() != 0) {
            double thanhtien = 0;
            tongtien = 0;
            for (int i = 0; i < listGH.size(); i++) {
                Object[] sp = listGH.get(i);
                int sl = Integer.parseInt(sp[7].toString());
                double gia = Double.parseDouble(sp[5].toString());
                thanhtien = sl * gia;
                tongtien += thanhtien;
            }
            if (txtPhiDH.getText().isEmpty()) {
                phiKhac = 0;
            } else {
                phiKhac = Double.parseDouble(txtPhiDH.getText());
            }
            thue = tongtien * 0.1;
            tienCanTra = tongtien + thue + phiKhac - giamGia;
            lblTienDH.setText(XCurrency.toCurrency(tongtien));
            lblGiamgiaDH.setText(XCurrency.toCurrency(giamGia));
            lblKhachcantraDH.setText(XCurrency.toCurrency(tienCanTra));
        } else if (listGH1.size() != 0) {
            double thanhtien = 0;
            tongtien = 0;
            for (int i = 0; i < listGH1.size(); i++) {
                Object[] sp = listGH1.get(i);
                int sl = Integer.parseInt(sp[2].toString());
                double gia = Double.parseDouble(sp[3].toString());
                thanhtien = sl * gia;
                tongtien += thanhtien;
            }
            String matvv = txtMaTheTV.getText();
            double giam = 0, phikhac = 0, thue = 0;
            for (int i = 0; i < listTTV.size(); i++) {
                if (listTTV.get(i).getMaTheTV().equals(matvv)) {
                    giam = tongtien * 0.03;
                }
            }
            if (txtPhiDH.getText().isEmpty()) {
                phikhac = 0;
            } else {
                phikhac = Double.parseDouble(txtPhiDH.getText());
            }
            thue = tongtien * 0.1;
            tienCanTra = tongtien + thue + phikhac - giam;
            lblTienDH.setText(XCurrency.toCurrency(tongtien));
            lblGiamgiaDH.setText(XCurrency.toCurrency(giam));
            lblKhachcantraDH.setText(XCurrency.toCurrency(tienCanTra));
        } else {
            System.out.println("Sai sai sai!");
        }
    }

    void taoHDTQ() {
        if (listGH.size() != 0) {
            double thanhtien = 0;
            tongtien = 0;
            for (int i = 0; i < listGH.size(); i++) {
                Object[] sp = listGH.get(i);
                int sl = Integer.parseInt(sp[7].toString());
                double gia = Double.parseDouble(sp[5].toString());
                thanhtien = sl * gia;
                tongtien += thanhtien;
            }
            if (txtPhikhac.getText().isEmpty()) {
                phiKhac = 0;
            } else {
                phiKhac = Double.parseDouble(txtPhikhac.getText());
            }
            thue = tongtien * 0.1;
            tienCanTra = tongtien + thue + phiKhac - giamGia;
            lblTongTien.setText(XCurrency.toCurrency(tongtien));
            lblGiamGia.setText(XCurrency.toCurrency(giamGia));
            lblTienCanTra.setText(XCurrency.toCurrency(tienCanTra));
        } else if (listGH1.size() != 0) {
            double thanhtien = 0;
            tongtien = 0;
            for (int i = 0; i < listGH1.size(); i++) {
                Object[] sp = listGH1.get(i);
                int sl = Integer.parseInt(sp[2].toString());
                double gia = Double.parseDouble(sp[3].toString());
                thanhtien = sl * gia;
                tongtien += thanhtien;
            }
            String matvv = txtMaTheTV.getText();
            double giam = 0, phikhac = 0, thue = 0;
            for (int i = 0; i < listTTV.size(); i++) {
                if (listTTV.get(i).getMaTheTV().equals(matvv)) {
                    giam = tongtien * 0.03;
                }
            }
            if (txtPhikhac.getText().isEmpty()) {
                phikhac = 0;
            } else {
                phikhac = Double.parseDouble(txtPhikhac.getText());
            }
            thue = tongtien * 0.1;
            tienCanTra = tongtien + thue + phikhac - giam;
            lblTongTien.setText(XCurrency.toCurrency(tongtien));
            lblGiamGia.setText(XCurrency.toCurrency(giam));
            lblTienCanTra.setText(XCurrency.toCurrency(tienCanTra));
        } else {
            System.out.println("Sai sai sai!");
        }
    }

    void taoHoaDon() {
        try {
            lblTienTraLai.setText("");
            if (listGH.isEmpty() && listGH1.isEmpty()) {
                MsgBox.alert(this, "Giỏ hàng rỗng! Không thể tạo hóa đơn.");
                return;
            }
            if (ckbDatHang.isSelected()) {
                boolean hoi = MsgBox.confirm(this, "Bạn muốn tạo hóa đơn đặt hàng không?");
                if (hoi == false) {
                    return;
                }
                pnlTab.setSelectedIndex(1);
                if (listGH.size() != 0) {
                    double thanhtien = 0;
                    tongtien = 0;
                    for (int i = 0; i < listGH.size(); i++) {
                        Object[] sp = listGH.get(i);
                        int sl = Integer.parseInt(sp[7].toString());
                        double gia = Double.parseDouble(sp[5].toString());
                        thanhtien = sl * gia;
                        tongtien += thanhtien;
                    }
                    if (txtPhiDH.getText().isEmpty()) {
                        phiKhac = 0;
                    } else {
                        phiKhac = Double.parseDouble(txtPhiDH.getText());
                    }
                    thue = tongtien * 0.1;
                    tienCanTra = tongtien + thue + phiKhac - giamGia;
                    lblTienDH.setText(XCurrency.toCurrency(tongtien));
                    lblGiamgiaDH.setText(XCurrency.toCurrency(giamGia));
                    lblKhachcantraDH.setText(XCurrency.toCurrency(tienCanTra));
                } else if (listGH1.size() != 0) {
                    double thanhtien = 0;
                    tongtien = 0;
                    for (int i = 0; i < listGH1.size(); i++) {
                        Object[] sp = listGH1.get(i);
                        int sl = Integer.parseInt(sp[2].toString());
                        double gia = Double.parseDouble(sp[3].toString());
                        thanhtien = sl * gia;
                        tongtien += thanhtien;
                    }
                    String matvv = txtMaTheTV.getText();
                    double giam = 0, phikhac = 0, thue = 0;
                    for (int i = 0; i < listTTV.size(); i++) {
                        if (listTTV.get(i).getMaTheTV().equals(matvv)) {
                            giam = tongtien * 0.03;
                        }
                    }
                    if (txtPhiDH.getText().isEmpty()) {
                        phikhac = 0;
                    } else {
                        phikhac = Double.parseDouble(txtPhiDH.getText());
                    }
                    thue = tongtien * 0.1;
                    tienCanTra = tongtien + thue + phikhac - giam;
                    lblTienDH.setText(XCurrency.toCurrency(tongtien));
                    lblGiamgiaDH.setText(XCurrency.toCurrency(giam));
                    lblKhachcantraDH.setText(XCurrency.toCurrency(tienCanTra));
                } else {
                    System.out.println("Sai sai sai!");
                }

            } else {
                boolean hoi = MsgBox.confirm(this, "Bạn muốn tạo hóa đơn tại quầy không?");
                if (hoi == false) {
                    return;
                }
                if (listGH.size() != 0) {
                    double thanhtien = 0;
                    tongtien = 0;
                    for (int i = 0; i < listGH.size(); i++) {
                        Object[] sp = listGH.get(i);
                        int sl = Integer.parseInt(sp[7].toString());
                        double gia = Double.parseDouble(sp[5].toString());
                        thanhtien = sl * gia;
                        tongtien += thanhtien;
                    }
                    if (txtPhikhac.getText().isEmpty()) {
                        phiKhac = 0;
                    } else {
                        phiKhac = Double.parseDouble(txtPhikhac.getText());
                    }
                    thue = tongtien * 0.1;
                    tienCanTra = tongtien + thue + phiKhac - giamGia;
                    lblTongTien.setText(XCurrency.toCurrency(tongtien));
                    lblGiamGia.setText(XCurrency.toCurrency(giamGia));
                    lblTienCanTra.setText(XCurrency.toCurrency(tienCanTra));
                } else if (listGH1.size() != 0) {
                    double thanhtien = 0;
                    tongtien = 0;
                    for (int i = 0; i < listGH1.size(); i++) {
                        Object[] sp = listGH1.get(i);
                        int sl = Integer.parseInt(sp[2].toString());
                        double gia = Double.parseDouble(sp[3].toString());
                        thanhtien = sl * gia;
                        tongtien += thanhtien;
                    }
                    String matvv = txtMaTheTV.getText();
                    double giam = 0, phikhac = 0, thue = 0;
                    for (int i = 0; i < listTTV.size(); i++) {
                        if (listTTV.get(i).getMaTheTV().equals(matvv)) {
                            giam = tongtien * 0.03;
                        }
                    }
                    if (txtPhikhac.getText().isEmpty()) {
                        phikhac = 0;
                    } else {
                        phikhac = Double.parseDouble(txtPhikhac.getText());
                    }
                    thue = tongtien * 0.1;
                    tienCanTra = tongtien + thue + phikhac - giam;
                    lblTongTien.setText(XCurrency.toCurrency(tongtien));
                    lblGiamGia.setText(XCurrency.toCurrency(giam));
                    lblTienCanTra.setText(XCurrency.toCurrency(tienCanTra));
                } else {
                    System.out.println("Sai sai sai!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    void clearSP() {
        try {
            ImageIcon icon = new ImageIcon();
            lblAnh1.setIcon(icon);
            lblDongsp1.setText("");
            lblKL1.setText("");
            lblTensp1.setText("");
            lblDongia.setText("");

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
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private List<Object[]> loadTrang(List<Object[]> list, int index) {
//        List<Object[]> list = bh.loadSanPham();
        try {
            int test = list.size() / 4;
            int du = list.size() % 4;

            if (du > 0) {
                maxIndex = test + 1;
            }
            if (du == 0) {
                maxIndex = test;
            }
            lblSP.setText(String.valueOf(index + 1) + "/" + String.valueOf(maxIndex));
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
//            System.out.println("Trang");
            return listSP1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void check_HD() {
        try {
            List<Object[]> list = bh.duyet_HD();
            for (int i = 0; i < list.size(); i++) {
                Object[] sp = list.get(i);
                int idhd = Integer.parseInt(sp[5].toString());
                int idctsp = Integer.parseInt(sp[4].toString());
                int soLuongTon = Integer.parseInt(sp[1].toString());
                int soLuong = Integer.parseInt(sp[2].toString());
                int soLuongThuc = soLuongTon + soLuong;
                String trangThai = "Hóa đơn hủy khi chờ";
                Date ngay = (Date) sp[0];

                if (ngay.equals(new Date()) == false) {
                    bh.update_TrangThai_HoaDon_By_ID(trangThai, idhd);
                    bh.update_soLuongTon_CTSP_By_ID(soLuongThuc, idctsp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    int check_So(String txt) {
        if (txt == null) {
            int kq = 0;
            return kq;
        }
        try {
            int kq = Integer.parseInt(txt);
            if (kq <= 0) {
                MsgBox.alert(this, "Số lượng phải lớn hơn 0");
                kq = 0;
            } else if (kq != (int) kq) {
                MsgBox.alert(this, "Bạn phải nhập số nguyên!");
                kq = 0;
            } else {
                kq = kq;
            }
            return kq;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            MsgBox.alert(this, "Bạn phải nhập vào số nguyên dương!");
            throw new RuntimeException(e);
        }
    }

    void thanhToanDH() {
        try {
            if (listGH.isEmpty() && listGH1.isEmpty()) {
                MsgBox.alert(this, "Bạn chưa có gì để thanh toán!");
                return;
            }
            if (lblTienDH.getText().equals("")) {
                MsgBox.alert(this, "Hóa đơn chưa được tạo!");
                return;
            }
            if (txtSDT.getText().equals("") || txtNhanhang.getText().equals("")) {
                MsgBox.alert(this, "Bạn không thể để trống số điện thoại hoặc địa chỉ nhận!");
                return;
            }
            boolean check = MsgBox.confirm(this, "Bạn chắc chắn đã gọi xác nhận với khách hàng về đơn hàng này?");
            if (check == false) {
                return;
            }
            String manv = lblMaNVDH.getText();
            int idnv = 0;
            String madh;
            if (listDH.isEmpty()) {
                madh = "DH0001";
            } else {
                madh = listDH.get(listDH.size() - 1).getMaDatHang();
            }
            int mahdd = Integer.parseInt(madh.substring(2));
            mahdd++;
            String thetv = txtMaTVDH.getText();
            int idttv = 0;
            for (int i = 0; i < listTTV.size(); i++) {
                if (listTTV.get(i).getMaTheTV().equals(thetv)) {
                    idttv = listTTV.get(i).getIDTheTV();
                }
            }
            for (int i = 0; i < ListNV.size(); i++) {
                if (ListNV.get(i).getMaNV().equals(manv)) {
                    idnv = ListNV.get(i).getIDNhanVien();
                }
            }
            String httt = cbbHinhthucThanhtoanDH.getSelectedItem().toString();
            int idht = 0;
            for (int i = 0; i < listHTTT.size(); i++) {
                if (listHTTT.get(i).getTenHinhThucThanhToan().equals(httt)) {
                    idht = listHTTT.get(i).getIDHinhThucThanhToan();
                }
            }
            Date Ngaytao = new SimpleDateFormat("yyyy/MM/dd").parse(lblNgayTaoDH.getText());
            String ghichu = txtGhichuDH.getText();
            String trangt = "Đang giao";
            String diachi = txtNhanhang.getText();
            String sdt = txtSDT.getText();
            String hoten = txtHoTenDH.getText();
            DatHang dh = new DatHang();
            dh.setMaDatHang("DH" + String.valueOf(mahdd));
            dh.setDiaChi(diachi);
            dh.setGhiChu(ghichu);
            dh.setHoTenKhachHang(hoten);
            dh.setIDHinhThucThanhToan(idht);
            dh.setIDNhanVien(idnv);
            dh.setNgayLap(Ngaytao);
            dh.setSDT(sdt);
            dh.setTongTien(tienCanTra);
            dh.setTrangThai(trangt);
            if (!thetv.isEmpty()) {
                dh.setIDTheTV(idttv);
                bh.insert_DH(dh);
            } else {
                dh.setIDTheTV(0);
                bh.insert_DH(dh);
            }
            List<DatHang> list = bh.loadListDH();
            int idhd2 = list.get(list.size() - 1).getIDDatHang();
            for (int i = 0; i < listGH.size(); i++) {
                Object[] sp = listGH.get(i);
                String masp = sp[0].toString();
                int idctsp = Integer.parseInt(sp[8].toString());
                int sl = Integer.parseInt(sp[7].toString());
                double dongia = Double.parseDouble(sp[5].toString());
                ChiTietDatHang ctdh = new ChiTietDatHang();
                ctdh.setIDDatHang(idhd2);
                ctdh.setIDChiTietSP(idctsp);
                ctdh.setSoLuong(sl);
                ctdh.setDonGia(dongia);
                bh.insret_CTDH(ctdh);

                listCTSP = bh.loadListCTSP();
                for (int j = 0; j < listCTSP.size(); j++) {
                    if (listCTSP.get(j).getIDChiTietSP() == idctsp) {
                        soLuongTon = listCTSP.get(j).getSoLuongTon() - sl;
                        listCTSP.get(j).setSoLuongTon(soLuongTon);
                        bh.update_soLuongTon_CTSP_By_ID(soLuongTon, idctsp);
                    }
                }
            }
            MsgBox.alert(this, "Thanh toán thành công!");
            listGH.removeAll(listGH);
            listGH1.removeAll(listGH1);
            clearDH();
//            check_HD();
            loadGH1();
            loadGH();
            fillTableHD();
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Thanh toán thất bại");
            throw new RuntimeException(e);
        }
    }

    void thanhToan() {
        try {
            if (listGH.isEmpty() && listGH1.isEmpty()) {
                MsgBox.alert(this, "Bạn chưa có gì để thanh toán!");
                return;
            }
            if (lblTongTien.getText().equals("")) {
                MsgBox.alert(this, "Hóa đơn chưa được tạo!");
                return;
            }
            if (!(ckbChoThanhToan.isSelected())) {
                StringBuilder sb = new StringBuilder();
                Validator.checkMoney(txtTienKhachTra, sb);
                if (sb.length() > 0) {
                    MsgBox.alert(this, sb.toString());
                    return;
                }
                double khachtra = Double.parseDouble(txtTienKhachTra.getText());
                double tiendu = khachtra - tienCanTra;
                if (tiendu < 0) {
                    MsgBox.alert(this, "Tiền thanh toán chưa đủ!");
                    return;
                } else {
                    lblTienTraLai.setText(String.valueOf(tiendu));
                }
            }

            if (listGH1.size() != 0) {
                if (ckbChoThanhToan.isSelected()) {
                    MsgBox.alert(this, "Hóa đơn đã ở trong trạng thái chờ trước đó, không thể tiếp tục chờ!");
                    return;
                }
                update_SoLuongTon_HoaDonCho();
                List<Object[]> list1 = bh.xuLyHoaDon(hoaDon);
                if (listGH1.size() > list1.size()) {
                    for (int j = list1.size(); j < listGH1.size(); j++) {
                        for (int i = 0; i < listGH1.size(); i++) {
                            Object[] sp = listGH1.get(i);
                            int idctsp = Integer.parseInt(sp[6].toString());
                            int sl = Integer.parseInt(sp[2].toString());
                            double dongia = Double.parseDouble(sp[5].toString());
                            ChiTietHoaDon cthd = new ChiTietHoaDon();
                            cthd.setIDHoaDon(hoaDon);
                            cthd.setIDChiTietSP(idctsp);
                            cthd.setSoLuong(sl);
                            cthd.setDonGia(dongia);
                            bh.insert_CTHD(cthd);
                        }
                    }
                }
                String abc = "Đã thanh toán";
                bh.update_TrangThai_HoaDon_By_ID(abc, hoaDon);
                hoaDon = 0;
                MsgBox.alert(this, "Thanh toán thành công!");
            } else {
                String manv = lblMaNV.getText();
                int idnv = 0;
                String mahd = listHD.get(listHD.size() - 1).getMaHD();
                int mahdd = Integer.parseInt(mahd.substring(2));
                mahdd++;
                String thetv = txtMaTheTV.getText();
                int idttv = 0;
                for (int i = 0; i < listTTV.size(); i++) {
                    if (listTTV.get(i).getMaTheTV().equals(thetv)) {
                        idttv = listTTV.get(i).getIDTheTV();
                    }
                }
                for (int i = 0; i < ListNV.size(); i++) {
                    if (ListNV.get(i).getMaNV().equals(manv)) {
                        idnv = ListNV.get(i).getIDNhanVien();
                    }
                }
                String httt = cbbHinhThucThanhToan.getSelectedItem().toString();
                int idht = 0;
                for (int i = 0; i < listHTTT.size(); i++) {
                    if (listHTTT.get(i).getTenHinhThucThanhToan().equals(httt)) {
                        idht = listHTTT.get(i).getIDHinhThucThanhToan();
                    }
                }
                Date Ngaytao = new SimpleDateFormat("yyyy/MM/dd").parse(lblNgayTao.getText());
                String ghichu = txtGhichuHD.getText();
                String trangt = ckbChoThanhToan.isSelected() ? "Chờ thanh toán" : "Đã thanh toán";
                HoaDon hd = new HoaDon();
                hd.setMaHD("HD" + String.valueOf(mahdd));
                hd.setIDNhanVien(idnv);
                hd.setIDHinhThucThanhToan(idht);
                hd.setNgayLap(Ngaytao);
                hd.setGhiChu(ghichu);
                hd.setTongTien(tienCanTra);
                hd.setTrangThai(trangt);
                if (!thetv.isEmpty()) {
                    hd.setIDTheTV(idttv);
                    bh.insert_HD(hd);
                } else {
                    hd.setIDTheTV(NULL);
                    bh.insert_HD(hd);
                }
                List<HoaDon> list = bh.loadListHD();
                int idhd2 = list.get(list.size() - 1).getIDHoaDon();
                for (int i = 0; i < listGH.size(); i++) {
                    Object[] sp = listGH.get(i);
                    String masp = sp[0].toString();
                    int idctsp = Integer.parseInt(sp[8].toString());
                    int sl = Integer.parseInt(sp[7].toString());
                    double dongia = Double.parseDouble(sp[5].toString());
                    ChiTietHoaDon cthd = new ChiTietHoaDon();
                    cthd.setIDHoaDon(idhd2);
                    cthd.setIDChiTietSP(idctsp);
                    cthd.setSoLuong(sl);
                    cthd.setDonGia(dongia);
                    bh.insert_CTHD(cthd);

                    listCTSP = bh.loadListCTSP();
                    for (int j = 0; j < listCTSP.size(); j++) {
                        if (listCTSP.get(j).getIDChiTietSP() == idctsp) {
                            soLuongTon = listCTSP.get(j).getSoLuongTon() - sl;
                            listCTSP.get(j).setSoLuongTon(soLuongTon);
                            bh.update_soLuongTon_CTSP_By_ID(soLuongTon, idctsp);
                        }
                    }
                }
                MsgBox.alert(this, "Thanh toán thành công!");
            }
            listGH.removeAll(listGH);
            listGH1.removeAll(listGH1);
//            check_HD();
            loadGH1();
            loadGH();
            fillTableHD();
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Thanh toán thất bại");
            throw new RuntimeException(e);
        }
    }

    void loadGH() {
        try {
            listGH1.removeAll(listGH1);
            DefaultTableModel model = (DefaultTableModel) tblGiohang.getModel();
            model.setRowCount(0);
            for (Object[] ak : listGH) {
                double thanhtien = Integer.parseInt(ak[7].toString()) * Double.parseDouble(ak[5].toString());
                model.addRow(new Object[]{
                    ak[0], ak[2], ak[7], Double.parseDouble(ak[5].toString()), thanhtien
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void loadSP(List<Object[]> list) {
//        List<Object[]> list = loadTrang(index);
        try {
            if (list.size() == 0) {
                JOptionPane.showMessageDialog(this, "Danh sách sản phẩm rỗng");
            } else if (list.size() == 1) {
                sp1 = list.get(0);
                String anh1 = "img\\SanPham/" + sp1[6].toString();
                ImageIcon icon1 = new ImageIcon(new ImageIcon(anh1).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
                lblAnh1.setIcon(icon1);
                lblDongsp1.setText(sp1[1].toString());
                lblTensp1.setText(sp1[2].toString() + " - " + sp1[3].toString());
                lblKL1.setText(sp1[4].toString());
                lblDongia.setText(XCurrency.toCurrency(Double.parseDouble(sp1[5].toString())));

                ImageIcon icon = new ImageIcon();
                lblAnh2.setIcon(icon);
                lblDongsp2.setText("");
                lblTensp2.setText("");
                lblKL2.setText("");
                lblDongia1.setText("");

                lblAnh3.setIcon(icon);
                lblDongsp3.setText("");
                lblTensp3.setText("");
                lblKL3.setText("");
                lblDongia2.setText("");

                lblAnh4.setIcon(icon);
                lblDongsp4.setText("");
                lblTensp4.setText("");
                lblKL4.setText("");
                lblDongia3.setText("");
            } else if (list.size() == 2) {
                sp1 = list.get(0);
                String anh1 = "img\\SanPham/" + sp1[6].toString();
                ImageIcon icon1 = new ImageIcon(new ImageIcon(anh1).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
                lblAnh1.setIcon(icon1);
                lblDongsp1.setText(sp1[1].toString());
                lblTensp1.setText(sp1[2].toString() + " - " + sp1[3].toString());
                lblKL1.setText(sp1[4].toString());
                lblDongia.setText(XCurrency.toCurrency(Double.parseDouble(sp1[5].toString())));
                sp2 = list.get(1);
                String anh2 = "SanPham/" + sp2[6].toString();
                ImageIcon icon2 = new ImageIcon(new ImageIcon(anh2).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
                lblAnh2.setIcon(icon2);
                lblDongsp2.setText(sp2[1].toString());
                lblTensp2.setText(sp2[2].toString() + " - " + sp2[3].toString());
                lblKL2.setText(sp2[4].toString());
                lblDongia1.setText(XCurrency.toCurrency(Double.parseDouble(sp2[5].toString())));

                ImageIcon icon = new ImageIcon();
                lblAnh3.setIcon(icon);
                lblDongsp3.setText("");
                lblTensp3.setText("");
                lblKL3.setText("");
                lblDongia2.setText("");

                lblAnh4.setIcon(icon);
                lblDongsp4.setText("");
                lblTensp4.setText("");
                lblKL4.setText("");
                lblDongia3.setText("");
            } else if (list.size() == 3) {
                sp1 = list.get(0);
                String anh1 = "img\\SanPham/" + sp1[6].toString();
                ImageIcon icon1 = new ImageIcon(new ImageIcon(anh1).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
                lblAnh1.setIcon(icon1);
                lblDongsp1.setText(sp1[1].toString());
                lblTensp1.setText(sp1[2].toString() + " - " + sp1[3].toString());
                lblKL1.setText(sp1[4].toString());
                lblDongia.setText(XCurrency.toCurrency(Double.parseDouble(sp1[5].toString())));

                sp2 = list.get(1);
                String anh2 = "img\\SanPham/" + sp2[6].toString();
                ImageIcon icon2 = new ImageIcon(new ImageIcon(anh2).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
                lblAnh2.setIcon(icon2);
                lblDongsp2.setText(sp2[1].toString());
                lblTensp2.setText(sp2[2].toString() + " - " + sp2[3].toString());
                lblKL2.setText(sp2[4].toString());
                lblDongia1.setText(XCurrency.toCurrency(Double.parseDouble(sp2[5].toString())));

                sp3 = list.get(2);
                String anh3 = "img\\SanPham/" + sp3[6].toString();
                ImageIcon icon3n = new ImageIcon(new ImageIcon(anh3).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
                lblAnh3.setIcon(icon3n);
                lblDongsp3.setText(sp3[1].toString());
                lblTensp3.setText(sp3[2].toString() + " - " + sp3[3].toString());
                lblKL3.setText(sp3[4].toString());
                lblDongia2.setText(XCurrency.toCurrency(Double.parseDouble(sp3[5].toString())));

                ImageIcon icon = new ImageIcon();
                lblAnh4.setIcon(icon);
                lblDongsp4.setText("");
                lblTensp4.setText("");
                lblKL4.setText("");
                lblDongia3.setText("");
            } else {
                sp1 = list.get(0);
                String anh1 = "img\\SanPham/" + sp1[6].toString();
                ImageIcon icon1 = new ImageIcon(new ImageIcon(anh1).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
                lblAnh1.setIcon(icon1);
                lblDongsp1.setText(sp1[1].toString());
                lblTensp1.setText(sp1[2].toString() + " - " + sp1[3].toString());
                lblKL1.setText(sp1[4].toString());
                lblDongia.setText(XCurrency.toCurrency(Double.parseDouble(sp1[5].toString())));

                sp2 = list.get(1);
                String anh2 = "img\\SanPham/" + sp2[6].toString();
                ImageIcon icon2 = new ImageIcon(new ImageIcon(anh2).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
                lblAnh2.setIcon(icon2);
                lblDongsp2.setText(sp2[1].toString());
                lblTensp2.setText(sp2[2].toString() + " - " + sp2[3].toString());
                lblKL2.setText(sp2[4].toString());
                lblDongia1.setText(XCurrency.toCurrency(Double.parseDouble(sp2[5].toString())));

                sp3 = list.get(2);
                String anh3 = "img\\SanPham/" + sp3[6].toString();
                ImageIcon icon3 = new ImageIcon(new ImageIcon(anh3).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
                lblAnh3.setIcon(icon3);
                lblDongsp3.setText(sp3[1].toString());
                lblTensp3.setText(sp3[2].toString() + " - " + sp3[3].toString());
                lblKL3.setText(sp3[4].toString());
                lblDongia2.setText(XCurrency.toCurrency(Double.parseDouble(sp3[5].toString())));

                sp4 = list.get(3);
                String anh4 = "img\\SanPham/" + sp4[6].toString();
                ImageIcon icon4 = new ImageIcon(new ImageIcon(anh4).getImage().getScaledInstance(180, 185, Image.SCALE_DEFAULT));
                lblAnh4.setIcon(icon4);
                lblDongsp4.setText(sp4[1].toString());
                lblTensp4.setText(sp4[2].toString() + " - " + sp4[3].toString());
                lblKL4.setText(sp4[4].toString());
                lblDongia3.setText(XCurrency.toCurrency(Double.parseDouble(sp4[5].toString())));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    void fillTableHD() {
        String cbb = cbbLoaiHD.getSelectedItem().toString();
        DefaultTableModel model = (DefaultTableModel) tblDSHoaDon.getModel();
        model.setRowCount(0);
        List<Object[]> list = bh.loadHoaDon(cbb);
        for (Object[] hd : list) {
            model.addRow(hd);
        }
    }

    void xuLyHD() {
        String mahd = tblDSHoaDon.getValueAt(index, 0).toString();
        listHD = bh.loadListHD();
        for (int i = 0; i < listHD.size(); i++) {
            if (listHD.get(i).getMaHD().equals(mahd)) {
                hoaDon = listHD.get(i).getIDHoaDon();
            }
        }
        listGH1 = bh.xuLyHoaDon(hoaDon);
        loadGH1();
    }

    void xulyDH() {
        String madh = tblDSHoaDon.getValueAt(index, 0).toString();
        List<Object[]> list = bh.xulyDH(madh);
        DefaultTableModel model = (DefaultTableModel) tblGiohang.getModel();
        model.setRowCount(0);
        for (Object[] ob : list) {
            model.addRow(ob);
        }
    }

    void loadGH1() {
        DefaultTableModel model = (DefaultTableModel) tblGiohang.getModel();
        model.setRowCount(0);
        for (Object[] arn : listGH1) {
            model.addRow(arn);
        }
    }

    void update_SoLuongTon_HoaDonCho() {
        List<Object[]> list = bh.xuLyHoaDon(hoaDon);
        for (int i = 0; i < list.size(); i++) {
            Object[] sp = listGH1.get(i);
            int sl = Integer.parseInt(sp[2].toString());
            int id = Integer.parseInt(sp[6].toString());
            Object[] spp = list.get(i);
            int ab = Integer.parseInt(spp[2].toString());
            for (int j = 0; j < listCTSP.size(); j++) {
                if (id == listCTSP.get(j).getIDChiTietSP()) {
                    soLuongTon = listCTSP.get(j).getSoLuongTon();
                }
            }
//            System.out.println(id);
            if (sl != ab) {
                if (sl > ab) {
                    thayDoi = sl - ab;
                    soLuongTon += thayDoi;
                    System.out.println(soLuongTon);
                    bh.update_soLuongTon_CTSP_By_ID(soLuongTon, id);
                } else {
                    thayDoi = ab - sl;
                    soLuongTon -= thayDoi;
                    System.out.println(soLuongTon);
                    bh.update_soLuongTon_CTSP_By_ID(soLuongTon, id);
                }
            }
        }
    }

    void update_SoLuongTon_HoaDon_DG(String ma) {
        List<Object[]> list = bh.xulyDH(ma);
        for (int i = 0; i < list.size(); i++) {
            Object[] sp = list.get(i);
            int sl = Integer.parseInt(sp[2].toString());
            int id = Integer.parseInt(sp[6].toString());
            for (int j = 0; j < listCTSP.size(); j++) {
                if (id == listCTSP.get(j).getIDChiTietSP()) {
                    soLuongTon = listCTSP.get(j).getSoLuongTon();
                    soLuongTon += sl;
                    bh.update_soLuongTon_CTSP_By_ID(soLuongTon, id);
                }
            }
//            Object[] spp = list.get(i);
//            int ab = Integer.parseInt(spp[2].toString());
//            for (int j = 0; j < listCTSP.size(); j++) {
//                if (id == listCTSP.get(j).getIDChiTietSP()) {
//                    soLuongTon = listCTSP.get(j).getSoLuongTon();
//                }
//            }
////            System.out.println(id);
//            if (sl != ab) {
//                if (sl > ab) {
//                    thayDoi = sl - ab;
//                    soLuongTon += thayDoi;
//                    System.out.println(soLuongTon);
//                    bh.update_soLuongTon_CTSP_By_ID(soLuongTon, id);
//                } else {
//                    thayDoi = ab - sl;
//                    soLuongTon -= thayDoi;
//                    System.out.println(soLuongTon);
//                    bh.update_soLuongTon_CTSP_By_ID(soLuongTon, id);
//                }
//            }
        }
    }

    void xuLySP(Object[] sp) {
        if (listGH1.size() != 0) {
            for (int i = 0; i < listGH1.size(); i++) {
                Object[] n = listGH1.get(i);
                int a = Integer.parseInt(n[6].toString());
                int b = Integer.parseInt(sp[8].toString());
                String ma = sp[0].toString();
//                for (int j = 0; j < listDSCB.size(); j++) {
//                    Object[] dt = listDSCB.get(j);
//                    String mscb = dt[0].toString();
//                    if (mscb.equals(ma)) {
//                        MsgBox.alert(this, "Sản phẩm hiện tại chưa tích hợp với phần mềm. bạn chưa thể thanh toán sản phẩm này tại đây!");
//                        return;
//                    }
//                }
                if (a == b) {
                    boolean hoi = MsgBox.confirm(this, "Sản phẩm đã tồn tại trong giỏ hàng. Bạn có muốn thay đổi số lượng?");
                    if (hoi == true) {
                        int sl = check_So(MsgBox.prompt1(this, "Nhập số lượng mua: "));
                        if (sl == 0) {
                            return;
                        }
                        n[2] = sl;
                        int slt = Integer.parseInt(sp[4].toString());
                        if (sl > slt) {
                            MsgBox.alert(this, "Xin lỗi bạn! Hàng tồn không đủ!");
                            return;
                        }
                        sl1 = slt - sl;
                        loadGH1();
                    }
                    return;
                }
            }
            int sl = check_So(MsgBox.prompt1(this, "Nhập số lượng mua: "));
            if (sl == 0) {
                return;
            }
            int slt = Integer.parseInt(sp[4].toString());
            if (sl > slt) {
                MsgBox.alert(this, "Xin lỗi bạn! Hàng tồn không đủ!");
                return;
            }
            sl1 = slt - sl;
            sp[7] = sl;
            int idctsp = Integer.parseInt(sp[8].toString());
            int idcthd = 0;
            for (int j = 0; j < listCTHD.size(); j++) {
                if (listCTHD.get(j).getIDHoaDon() == hoaDon && listCTHD.get(j).getIDChiTietSP() == idctsp) {
                    idcthd = listCTHD.get(j).getIDChiTietHD();
                }
            }
            Object[] spm = new Object[7];
            spm[0] = sp[0];
            spm[1] = sp[2];
            spm[2] = sp[7];
            spm[3] = sp[5];
            spm[4] = sl * Double.parseDouble(sp[5].toString());
            spm[5] = idcthd;
            spm[6] = sp[8];
            listGH1.add(spm);
            loadGH1();
        } else {
            if (listGH.size() != 0) {
                try {
                    for (int i = 0; i < listGH.size(); i++) {
                        Object[] n = listGH.get(i);
                        int a = Integer.parseInt(n[8].toString());
                        int b = Integer.parseInt(sp[8].toString());
                        String ma = sp[0].toString();
//                        for (int j = 0; j < listDSCB.size(); j++) {
//                            Object[] dt = listDSCB.get(j);
//                            String mscb = dt[0].toString();
//                            if (mscb.equals(ma)) {
//                                MsgBox.alert(this, "Sản phẩm hiện tại chưa tích hợp với phần mềm. bạn chưa thể thanh toán sản phẩm này tại đây!");
//                                return;
//                            }
//                        }
                        if (a == b) {
                            boolean hoi = MsgBox.confirm(this, "Sản phẩm đã tồn tại trong giỏ hàng.Bạn có muốn thay đổi số lượng?");
                            if (hoi == true) {
                                int sl = check_So(MsgBox.prompt1(this, "Nhập số lượng mua: "));
                                if (sl == 0) {
                                    return;
                                }
                                int slt = Integer.parseInt(sp[4].toString());
                                if (sl > slt) {
                                    MsgBox.alert(this, "Xin lỗi bạn! Hàng tồn không đủ!");
                                    return;
                                }
                                sl1 = slt - sl;
                                n[7] = sl;
                                loadGH();
                            }
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            int sl = check_So(MsgBox.prompt1(this, "Nhập số lượng mua: "));
            if (sl == 0) {
                return;
            }
            int slt = Integer.parseInt(sp[4].toString());
            if (sl > slt) {
                MsgBox.alert(this, "Xin lỗi bạn! Hàng tồn không đủ!");
                return;
            }
            sl1 = slt - sl;
            sp[7] = sl;
            listGH.add(sp);
            loadGH();
        }
    }

    public void fillTableDH() {
        DefaultTableModel model = (DefaultTableModel) tblDSHoaDon.getModel();
        model.setRowCount(0);
        List<DatHang> list = new ArrayList<>();
        listDH = bh.loadListDH();
        for (int i = 0; i < listDH.size(); i++) {
            if (listDH.get(i).getTrangThai().equalsIgnoreCase("Đang giao")) {
                list.add(listDH.get(i));
            }
        }
        ListNV = bh.loadListNV();
        for (DatHang dh : list) {
            String ma = null;
            for (int i = 0; i < ListNV.size(); i++) {
                if (ListNV.get(i).getIDNhanVien() == dh.getIDNhanVien()) {
                    ma = ListNV.get(i).getMaNV();
                }
            }
            model.addRow(new Object[]{
                dh.getMaDatHang(), ma, dh.getTongTien(), dh.getTrangThai()
            });
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
        pnlTab = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblMaNV = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtMaTheTV = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblNgayTao = new javax.swing.JLabel();
        btnXacNhanTheTV = new javax.swing.JButton();
        btnSuDungDiem = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        txtHoTenTV = new javax.swing.JTextField();
        txtDiemTV = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblGiamGia = new javax.swing.JLabel();
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
        txtTienKhachTra = new javax.swing.JTextField();
        ckbNhanHD = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        lblThueVAT = new javax.swing.JLabel();
        lblTienTraLai = new javax.swing.JLabel();
        btnThanhToan = new javax.swing.JButton();
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
        btnXanNhanDH = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtHoTenDH = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txtDiemTVDH = new javax.swing.JTextField();
        btnSuDung = new javax.swing.JButton();
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
        cbbHinhthucThanhtoanDH = new javax.swing.JComboBox<>();
        jLabel36 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtGhichuDH = new javax.swing.JTextArea();
        jLabel34 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        btnNewdonDH = new javax.swing.JButton();
        btnAdddonDH = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGiohang = new javax.swing.JTable();
        btnBo = new javax.swing.JButton();
        ckbDatHang = new javax.swing.JCheckBox();
        btnTaoHD = new javax.swing.JButton();
        btnQuet = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
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
        lblDongia = new javax.swing.JLabel();
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
        tblDSHoaDon = new javax.swing.JTable();
        cbbLoaiHD = new javax.swing.JComboBox<>();
        jLabel37 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        cbbUpdateTT = new javax.swing.JComboBox<>();
        jLabel40 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(107, 185, 240));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });

        jPanel13.setBackground(new java.awt.Color(107, 185, 240));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tạo hóa đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        pnlTab.setBackground(new java.awt.Color(107, 185, 240));

        jPanel3.setBackground(new java.awt.Color(107, 185, 240));

        jPanel5.setBackground(new java.awt.Color(107, 185, 240));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin chung", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel3.setText("Mã Nhân viên");

        lblMaNV.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblMaNV.setForeground(new java.awt.Color(0, 0, 255));
        lblMaNV.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel5.setText("Mã thẻ thành viên");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel6.setText("Ngày tạo");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel8.setText("Điểm thành viên");

        lblNgayTao.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblNgayTao.setForeground(new java.awt.Color(0, 0, 255));
        lblNgayTao.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        btnXacNhanTheTV.setBackground(new java.awt.Color(102, 255, 102));
        btnXacNhanTheTV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Tick.png"))); // NOI18N
        btnXacNhanTheTV.setText("Xác nhận");
        btnXacNhanTheTV.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXacNhanTheTV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanTheTVActionPerformed(evt);
            }
        });

        btnSuDungDiem.setBackground(new java.awt.Color(255, 255, 102));
        btnSuDungDiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Favourites.png"))); // NOI18N
        btnSuDungDiem.setText("Sử dụng");
        btnSuDungDiem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSuDungDiem.setEnabled(false);
        btnSuDungDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuDungDiemActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel29.setText("Họ tên thành viên");

        txtHoTenTV.setEditable(false);
        txtHoTenTV.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtHoTenTV.setForeground(new java.awt.Color(51, 51, 255));

        txtDiemTV.setEditable(false);
        txtDiemTV.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtDiemTV.setForeground(new java.awt.Color(51, 51, 255));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addComponent(jLabel29)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtMaTheTV, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnXacNhanTheTV))
                    .addComponent(txtHoTenTV)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtDiemTV, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSuDungDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lblMaNV, lblNgayTao});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                    .addComponent(lblMaNV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNgayTao))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaTheTV)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXacNhanTheTV))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHoTenTV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSuDungDiem)
                    .addComponent(txtDiemTV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblNgayTao, txtDiemTV, txtHoTenTV, txtMaTheTV});

        jPanel6.setBackground(new java.awt.Color(107, 185, 240));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chi tiết", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel10.setText("Tổng tiền hàng");

        lblTongTien.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblTongTien.setForeground(new java.awt.Color(0, 0, 255));
        lblTongTien.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel12.setText("Giảm giá");

        lblGiamGia.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblGiamGia.setForeground(new java.awt.Color(0, 0, 255));
        lblGiamGia.setText("0%");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel14.setText("Phí khác");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel15.setText("Hình thức thanh toán");

        cbbHinhThucThanhToan.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        cbbHinhThucThanhToan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền mặt" }));

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel16.setText("Khách cần trả");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel17.setText("Tiền trả lại");

        ckbChoThanhToan.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        ckbChoThanhToan.setText("Chờ thanh toán");
        ckbChoThanhToan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ckbChoThanhToanMouseClicked(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel18.setText("Ghi chú hóa đơn");

        txtGhichuHD.setColumns(20);
        txtGhichuHD.setRows(5);
        jScrollPane4.setViewportView(txtGhichuHD);

        lblTienCanTra.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTienCanTra.setForeground(new java.awt.Color(0, 0, 255));

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel31.setText("Khách trả");

        txtTienKhachTra.setName("Tiền khách trả"); // NOI18N

        ckbNhanHD.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        ckbNhanHD.setSelected(true);
        ckbNhanHD.setText("Nhận kèm hóa đơn");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel1.setText("Thuế VAT");

        lblThueVAT.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblThueVAT.setForeground(new java.awt.Color(255, 51, 51));
        lblThueVAT.setText("10%");

        lblTienTraLai.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTienTraLai.setForeground(new java.awt.Color(0, 0, 255));

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
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel1))
                                .addGap(45, 45, 45)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblGiamGia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtPhikhac, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(lblThueVAT, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(lblTongTien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addGap(53, 53, 53))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(74, 74, 74)))
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTienKhachTra, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTienCanTra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(74, 74, 74)
                                .addComponent(lblTienTraLai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(ckbChoThanhToan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ckbNhanHD)
                        .addGap(19, 19, 19))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbbHinhThucThanhToan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtPhikhac, txtTienKhachTra});

        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(lblTongTien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblGiamGia, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
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
                    .addComponent(txtTienKhachTra, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTienTraLai, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ckbNhanHD, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ckbChoThanhToan, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnThanhToan.setBackground(new java.awt.Color(102, 255, 102));
        btnThanhToan.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnThanhToan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Dollar.png"))); // NOI18N
        btnThanhToan.setText("Thanh toán");
        btnThanhToan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
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
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(119, 119, 119))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlTab.addTab("Hóa đơn", jPanel3);

        jPanel4.setBackground(new java.awt.Color(107, 185, 240));

        jPanel7.setBackground(new java.awt.Color(107, 185, 240));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin chung", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel19.setText("Mã nhân viên");

        lblMaNVDH.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblMaNVDH.setForeground(new java.awt.Color(0, 0, 255));

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel21.setText("Mã thẻ thành viên");

        txtMaTVDH.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel22.setText("Số điện thoại");

        txtSDT.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtSDT.setForeground(new java.awt.Color(51, 255, 51));

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel23.setText("Ngày tạo");

        lblNgayTaoDH.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblNgayTaoDH.setForeground(new java.awt.Color(0, 0, 255));

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel25.setText("Địa chỉ nhận");

        txtNhanhang.setColumns(20);
        txtNhanhang.setLineWrap(true);
        txtNhanhang.setRows(5);
        jScrollPane5.setViewportView(txtNhanhang);

        btnXanNhanDH.setBackground(new java.awt.Color(102, 255, 102));
        btnXanNhanDH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Tick.png"))); // NOI18N
        btnXanNhanDH.setText("Xác nhận");
        btnXanNhanDH.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXanNhanDH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXanNhanDHActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel2.setText("Họ tên thành viên");

        txtHoTenDH.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtHoTenDH.setForeground(new java.awt.Color(51, 255, 51));

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel30.setText(" Điểm thành viên");

        txtDiemTVDH.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtDiemTVDH.setForeground(new java.awt.Color(51, 255, 51));

        btnSuDung.setBackground(new java.awt.Color(255, 255, 102));
        btnSuDung.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Favourites.png"))); // NOI18N
        btnSuDung.setText("Sử dụng");
        btnSuDung.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSuDung.setEnabled(false);
        btnSuDung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuDungActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblNgayTaoDH, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(29, 29, 29))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel21)
                            .addComponent(jLabel2)
                            .addComponent(jLabel30)
                            .addComponent(jLabel25))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jScrollPane5)
                                .addGap(23, 23, 23))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblMaNVDH, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtHoTenDH, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                                            .addComponent(txtMaTVDH, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(btnXanNhanDH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                                            .addComponent(txtDiemTVDH, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(btnSuDung))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
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
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMaTVDH)
                        .addComponent(btnXanNhanDH)))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtHoTenDH, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDiemTVDH, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSuDung)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNgayTaoDH, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 51, Short.MAX_VALUE))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel7Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblMaNVDH, lblNgayTaoDH, txtDiemTVDH, txtHoTenDH, txtMaTVDH, txtSDT});

        jPanel8.setBackground(new java.awt.Color(107, 185, 240));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chi tiết", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel26.setText("Tiền hàng");

        lblTienDH.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblTienDH.setForeground(new java.awt.Color(255, 0, 51));

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel28.setText("Giảm giá");

        lblGiamgiaDH.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblGiamgiaDH.setForeground(new java.awt.Color(0, 0, 255));

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel32.setText("Phí khác");

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel33.setText("Khách cần trả");

        lblKhachcantraDH.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblKhachcantraDH.setForeground(new java.awt.Color(255, 0, 51));

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel35.setText("Hình thức thanh toán");

        cbbHinhthucThanhtoanDH.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel36.setText("Ghi chú");

        txtGhichuDH.setColumns(20);
        txtGhichuDH.setLineWrap(true);
        txtGhichuDH.setRows(5);
        jScrollPane6.setViewportView(txtGhichuDH);

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel34.setText("Thuế VAT");

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 0, 0));
        jLabel38.setText("10%");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel35)
                                    .addComponent(jLabel33)
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel32)
                                    .addComponent(jLabel34)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtPhiDH, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                    .addComponent(lblGiamgiaDH, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                    .addComponent(lblTienDH, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                    .addComponent(lblKhachcantraDH, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                    .addComponent(cbbHinhthucThanhtoanDH, 0, 194, Short.MAX_VALUE)
                                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap(81, Short.MAX_VALUE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTienDH, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGiamgiaDH, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhiDH, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblKhachcantraDH, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbHinhthucThanhtoanDH, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel8Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cbbHinhthucThanhtoanDH, txtPhiDH});

        btnNewdonDH.setBackground(new java.awt.Color(102, 255, 102));
        btnNewdonDH.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnNewdonDH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Add.png"))); // NOI18N
        btnNewdonDH.setText("Tạo mới");
        btnNewdonDH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewdonDHActionPerformed(evt);
            }
        });

        btnAdddonDH.setBackground(new java.awt.Color(102, 204, 255));
        btnAdddonDH.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnAdddonDH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/shipper.png"))); // NOI18N
        btnAdddonDH.setText("Giao hàng");
        btnAdddonDH.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdddonDH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdddonDHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 455, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(btnAdddonDH, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNewdonDH)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAdddonDH, btnNewdonDH});

        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(btnAdddonDH, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNewdonDH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlTab.addTab("Đặt hàng", jPanel4);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTab)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTab)
        );

        jPanel1.setBackground(new java.awt.Color(107, 185, 240));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Giỏ hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        tblGiohang.setAutoCreateRowSorter(true);
        tblGiohang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class
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
        tblGiohang.setRowHeight(25);
        tblGiohang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGiohangMouseClicked(evt);
            }
        });
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

        ckbDatHang.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        ckbDatHang.setForeground(new java.awt.Color(255, 255, 255));
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

        btnQuet.setBackground(new java.awt.Color(204, 204, 204));
        btnQuet.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnQuet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/barcode.png"))); // NOI18N
        btnQuet.setText("Quét mã");
        btnQuet.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnQuet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuetActionPerformed(evt);
            }
        });

        btnNew.setBackground(new java.awt.Color(255, 255, 255));
        btnNew.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Basket.png"))); // NOI18N
        btnNew.setText("Làm mới giỏ hàng");
        btnNew.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(ckbDatHang)
                        .addGap(18, 18, 18)
                        .addComponent(btnNew)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                    .addComponent(btnBo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTaoHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ckbDatHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnQuet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNew))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnBo, btnNew, btnQuet, btnTaoHD});

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

        lblSP.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        lblSP.setForeground(new java.awt.Color(255, 255, 255));
        lblSP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSP.setText("1/200");

        btnTimSP.setBackground(new java.awt.Color(255, 255, 255));
        btnTimSP.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
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
        lblAnh1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
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

        lblDongia.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblDongia.setForeground(new java.awt.Color(255, 0, 0));
        lblDongia.setText("jLabel7");

        jLabel4.setText("Đơn giá:");

        jLabel20.setText("Số lượng:");

        javax.swing.GroupLayout pnlSP1Layout = new javax.swing.GroupLayout(pnlSP1);
        pnlSP1.setLayout(pnlSP1Layout);
        pnlSP1Layout.setHorizontalGroup(
            pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSP1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSP1Layout.createSequentialGroup()
                        .addComponent(lblAnh1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 41, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSP1Layout.createSequentialGroup()
                        .addGroup(pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel20))
                        .addGap(18, 18, 18)
                        .addGroup(pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblKL1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDongia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(lblTensp1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDongsp1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlSP1Layout.setVerticalGroup(
            pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSP1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnh1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDongsp1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTensp1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKL1)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDongia)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDSSP.add(pnlSP1);

        pnlSP2.setBackground(new java.awt.Color(107, 185, 240));

        lblAnh2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblAnh2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
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
                    .addComponent(lblDongsp2, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
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
        lblAnh3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
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
                        .addGap(0, 49, Short.MAX_VALUE)))
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
        lblAnh4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
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
                        .addGap(0, 17, Short.MAX_VALUE)))
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
                        .addGap(72, 72, 72)
                        .addComponent(btnBackSP)
                        .addGap(18, 18, 18)
                        .addComponent(lblSP, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblNextSP)
                        .addGap(59, 59, 59)
                        .addComponent(btnResest, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlDSSP, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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

        tblDSHoaDon.setAutoCreateRowSorter(true);
        tblDSHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã HD", "Mã NV", "Tổng tiền", "Trạng thái"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDSHoaDon.setRowHeight(25);
        tblDSHoaDon.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblDSHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDSHoaDonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDSHoaDon);

        cbbLoaiHD.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        cbbLoaiHD.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbLoaiHDItemStateChanged(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Loại hóa đơn");

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Cập nhật trạng thái:");

        cbbUpdateTT.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        cbbUpdateTT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbbUpdateTTMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addGap(18, 18, 18)
                        .addComponent(cbbUpdateTT, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel37)
                        .addGap(27, 27, 27)
                        .addComponent(cbbLoaiHD, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbbLoaiHD)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel39)
                    .addComponent(cbbUpdateTT))
                .addGap(8, 8, 8))
        );

        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Vinamilk_logo.jpg"))); // NOI18N

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
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbbLoaiHDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbLoaiHDItemStateChanged
        String tt = cbbLoaiHD.getSelectedItem().toString();
        if (tt.equalsIgnoreCase("Chờ thanh toán")) {
            cbbUpdateTT.setEnabled(false);
            fillTableHD();
        } else {
            cbbUpdateTT.setEnabled(true);
            fillTableDH();
        }
//        fillTableHD();
    }//GEN-LAST:event_cbbLoaiHDItemStateChanged

    private void lblNextSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblNextSPActionPerformed
        index++;
        if (index >= maxIndex) {
            index = 0;
        }
        loadSP(loadTrang(listTrungGian, index));
    }//GEN-LAST:event_lblNextSPActionPerformed

    private void btnBackSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackSPActionPerformed
        index--;
        if (index < 0) {
            index = maxIndex - 1;
        }
        loadSP(loadTrang(listTrungGian, index));
    }//GEN-LAST:event_btnBackSPActionPerformed

    private void lblAnh1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnh1MouseClicked
        sl1 = 0;
        String ma = sp1[0].toString();
        for (int j = 0; j < listDSCB.size(); j++) {
            Object[] dt = listDSCB.get(j);
            String mscb = dt[0].toString();
            if (mscb.equals(ma)) {
                MsgBox.alert(this, "Sản phẩm hiện tại chưa tích hợp với phần mềm. bạn chưa thể thanh toán sản phẩm này tại đây!");
                return;
            }
        }
        xuLySP(sp1);
        lblKL1.setText(String.valueOf(sl1));
    }//GEN-LAST:event_lblAnh1MouseClicked

    private void lblAnh2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnh2MouseClicked
        sl1 = 0;
        String ma = sp2[0].toString();
        for (int j = 0; j < listDSCB.size(); j++) {
            Object[] dt = listDSCB.get(j);
            String mscb = dt[0].toString();
            if (mscb.equals(ma)) {
                MsgBox.alert(this, "Sản phẩm hiện tại chưa tích hợp với phần mềm. bạn chưa thể thanh toán sản phẩm này tại đây!");
                return;
            }
        }
        xuLySP(sp2);
        lblKL2.setText(String.valueOf(sl1));
    }//GEN-LAST:event_lblAnh2MouseClicked

    private void lblAnh3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnh3MouseClicked
        sl1 = 0;
        String ma = sp3[0].toString();
        for (int j = 0; j < listDSCB.size(); j++) {
            Object[] dt = listDSCB.get(j);
            String mscb = dt[0].toString();
            if (mscb.equals(ma)) {
                MsgBox.alert(this, "Sản phẩm hiện tại chưa tích hợp với phần mềm. bạn chưa thể thanh toán sản phẩm này tại đây!");
                return;
            }
        }
        xuLySP(sp3);
        lblKL3.setText(String.valueOf(sl1));
    }//GEN-LAST:event_lblAnh3MouseClicked

    private void lblAnh4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnh4MouseClicked
        sl1 = 0;
        String ma = sp4[0].toString();
        for (int j = 0; j < listDSCB.size(); j++) {
            Object[] dt = listDSCB.get(j);
            String mscb = dt[0].toString();
            if (mscb.equals(ma)) {
                MsgBox.alert(this, "Sản phẩm hiện tại chưa tích hợp với phần mềm. bạn chưa thể thanh toán sản phẩm này tại đây!");
                return;
            }
        }
        xuLySP(sp4);
        lblKL4.setText(String.valueOf(sl1));
    }//GEN-LAST:event_lblAnh4MouseClicked

    private void tblGiohangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGiohangMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            soLuongTon = 0;
            sl2 = 0;
            sl3 = 0;
            click = tblGiohang.getSelectedRow();
            if (listGH1.size() != 0) {
                capNhat = check_So(MsgBox.prompt1(this, "nhập số lượng sản phẩm"));
                Object[] sp = listGH1.get(click);
                int sl = Integer.parseInt(sp[2].toString());
                int id = Integer.parseInt(sp[6].toString());
                if (sl > capNhat) {
                    sl2 = sl - capNhat;
                    for (int i = 0; i < listDS.size(); i++) {
                        Object[] td = listDS.get(i);
                        int so = Integer.parseInt(td[8].toString());
                        int slt = Integer.parseInt(td[4].toString());
                        if (id == so) {
                            slt += sl2;
                            td[4] = slt;
                        }
                    }
                } else {
                    sl2 = capNhat - sl;
                    for (int i = 0; i < listDS.size(); i++) {
                        Object[] td = listDS.get(i);
                        int so = Integer.parseInt(td[8].toString());
                        int slt = Integer.parseInt(td[4].toString());
                        if (id == so) {
                            slt -= sl2;
                            td[4] = slt;
                        }
                    }
                }
                sp[2] = capNhat;
                sp[4] = capNhat * Double.parseDouble(sp[3].toString());
                loadGH1();
                loadSP(loadTrang(listDS, index));
            } else {
                MsgBox.alert(this, "Bạn không thể thay đổi tại đây! Hãy chọn sản phảm lần nữa để thay đổi số lượng!");
            }
        }
    }//GEN-LAST:event_tblGiohangMouseClicked

    private void btnBoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBoActionPerformed
        boolean trangt = MsgBox.confirm(this, "bạn có chắc muốn bỏ sản phẩm khỏi giỏ hàng?");
        if (trangt == true) {
            int chon = tblGiohang.getSelectedRow();
            sl4 = 0;
            if (chon < 0) {
                MsgBox.alert(this, "Bạn chưa chọn mục để loại bỏ!");
                return;
            }
            if (!(listGH.isEmpty())) {
                listGH.remove(chon);
                loadGH();
            } else {
                Object[] sp = listGH1.get(chon);
                int idctsp = Integer.parseInt(sp[6].toString());
                int idcthd = Integer.parseInt(sp[5].toString());
                int sl = Integer.parseInt(sp[2].toString());
                for (int i = 0; i < listCTSP.size(); i++) {
                    if (listCTSP.get(i).getIDChiTietSP() == idctsp) {
                        sl4 = listCTSP.get(i).getSoLuongTon();
                    }
                }
                sl4 += sl;
                bh.update_soLuongTon_CTSP_By_ID(sl4, idctsp);
                bh.delete_cthd(idcthd);
                listGH1.remove(chon);
                bh.delete_hd_rong();
                loadGH1();
            }
        }
    }//GEN-LAST:event_btnBoActionPerformed

    private void btnTimSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimSPActionPerformed
        timSP();
    }//GEN-LAST:event_btnTimSPActionPerformed

    private void btnResestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResestActionPerformed
        txtTimSP.setText("");
        listTrungGian = new ArrayList<>();
        listDS = bh.loadSanPham();
        listDSCB = bh.loadComBo();
        for (int i = 0; i < listDS.size(); i++) {
            listTrungGian.add(listDS.get(i));
        }
        for (int i = 0; i < listDSCB.size(); i++) {
            listTrungGian.add(listDSCB.get(i));
        }
        loadSP(loadTrang(listTrungGian, 0));
    }//GEN-LAST:event_btnResestActionPerformed

    private void btnTaoHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoHDActionPerformed
        taoHoaDon();
    }//GEN-LAST:event_btnTaoHDActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        
        if (tienCanTra >= 100000) {
            int diemThuong = (int) Math.floor(tienCanTra * 0.01 / 1000);
            theThanhVienDAO.updateDiem(diemThuong + "", txtMaTheTV.getText());
        }
        thanhToan();
        txtMaTheTV.setText("");
        txtHoTenTV.setText("");
        txtDiemTV.setText("");
        lblTongTien.setText("");
        lblGiamGia.setText("");
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void btnQuetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuetActionPerformed
        new QuetMaVachJFrame().setVisible(true);
    }//GEN-LAST:event_btnQuetActionPerformed

    private void tblDSHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSHoaDonMouseClicked
        if (evt.getClickCount() == 2) {
            listGH.removeAll(listGH);
            index = tblDSHoaDon.getSelectedRow();
            if (cbbLoaiHD.getSelectedItem().toString().equals("Chờ thanh toán")) {
                xuLyHD();
            } else {
                xulyDH();
            }
        }
    }//GEN-LAST:event_tblDSHoaDonMouseClicked

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        if (listGH.isEmpty() && listGH1.isEmpty()) {
            MsgBox.alert(this, "Danh sách giỏ hàng đang rỗng!");
            return;
        }
        if (!MsgBox.confirm(this, "Bạn có chắc muốn làm mới giỏ hàng?")) {
            return;
        }
        listGH.removeAll(listGH);
        loadGH();
        listGH1.removeAll(listGH1);
        loadGH1();
    }//GEN-LAST:event_btnNewActionPerformed

    private void ckbChoThanhToanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ckbChoThanhToanMouseClicked
        if (ckbChoThanhToan.isSelected()) {
            txtTienKhachTra.setEditable(false);
        } else {
            txtTienKhachTra.setEditable(true);
        }
    }//GEN-LAST:event_ckbChoThanhToanMouseClicked

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        if (getSanPhamQuetTuBarcode && sanPhamQuetTuBarcode != null) {
            xuLySP(sanPhamQuetTuBarcode);
            getSanPhamQuetTuBarcode = false;
        }
    }//GEN-LAST:event_formMouseEntered

    private void btnXacNhanTheTVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanTheTVActionPerformed
        try {
            StringBuilder sb = new StringBuilder();
            Validator.isNull(txtMaTheTV, "Mã thẻ thành viên chưa nhập", sb);
            if (sb.length() > 0) {
                MsgBox.alert(this, sb.toString());
                return;
            }
            TheThanhVien theThanhVien = theThanhVienDAO.selectById(txtMaTheTV.getText());
            if (theThanhVien != null) {
                txtHoTenTV.setText(theThanhVien.getTenKH());
                txtDiemTV.setText(theThanhVien.getDiem() + "");
                btnSuDungDiem.setEnabled(true);
            } else {
                txtHoTenTV.setText("");
                txtDiemTV.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnXacNhanTheTVActionPerformed

    private void btnSuDungDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuDungDiemActionPerformed
        try {
            if (MsgBox.confirm(this, "Bạn có muốn sử dụng điểm không?")) {
                giamGia += Double.parseDouble(txtDiemTV.getText()) * 1000;
                taoHoaDon();
                btnSuDungDiem.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSuDungDiemActionPerformed

    private void btnNewdonDHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewdonDHActionPerformed
        // TODO add your handling code here:
        clearDH();
    }//GEN-LAST:event_btnNewdonDHActionPerformed

    private void cbbUpdateTTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbbUpdateTTMouseClicked
        // TODO add your handling code here:
        int vitri = tblDSHoaDon.getSelectedRow();
        pnlTab.setSelectedIndex(1);
        if (vitri < 0) {
            MsgBox.alert(this, "Bạn chưa chọn hóa đơn để cập nhật");
            return;
        }
        boolean hoi = MsgBox.confirm(this, "Bạn có chắc muốn thay đổi trạng thái hóa đơn?");
        if (hoi == false) {
            return;
        }
        String madh = tblDSHoaDon.getValueAt(index, 0).toString();
        String tt = cbbUpdateTT.getSelectedItem().toString();
        if (tt.equalsIgnoreCase("Đã thanh toán")) {
            bh.update_TTDH(tt, madh);
        } else {
            String gichu = txtGhichuDH.getText();
            if (gichu.equals("")) {
                MsgBox.alert(this, "Bạn buộc phải thêm ghi chú lý do hóa đơn bị hủy!");
                return;
            }
            listGH1.removeAll(listGH1);
            listGH1 = bh.xulyDH(madh);
            update_SoLuongTon_HoaDon_DG(madh);
            bh.update_Ghichu_DH(gichu, madh);
            bh.update_TTDH(tt, madh);
            DefaultTableModel model = (DefaultTableModel) tblGiohang.getModel();
            model.setRowCount(0);
        }
        MsgBox.alert(this, "Cập nhật trạng thái thành công!");
        fillTableDH();
    }//GEN-LAST:event_cbbUpdateTTMouseClicked

    private void btnAdddonDHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdddonDHActionPerformed
        if (tienCanTra >= 100000) {
            int diemThuong = (int) Math.floor(tienCanTra * 0.01 / 1000);
            theThanhVienDAO.updateDiem(diemThuong + "", txtMaTVDH.getText());
        }
        thanhToanDH();
    }//GEN-LAST:event_btnAdddonDHActionPerformed

    private void btnXanNhanDHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXanNhanDHActionPerformed
        try {
            StringBuilder sb = new StringBuilder();
            Validator.isNull(txtMaTVDH, "Mã thẻ thành viên chưa nhập", sb);
            if (sb.length() > 0) {
                MsgBox.alert(this, sb.toString());
                return;
            }
            TheThanhVien theThanhVien = theThanhVienDAO.selectById(txtMaTVDH.getText());
            if (theThanhVien != null) {
                txtHoTenDH.setText(theThanhVien.getTenKH());
                txtDiemTVDH.setText(theThanhVien.getDiem() + "");
                txtSDT.setText(theThanhVien.getSDT());
                btnSuDung.setEnabled(true);
            } else {
                txtHoTenDH.setText("");
                txtDiemTVDH.setText("");
                txtSDT.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnXanNhanDHActionPerformed

    private void btnSuDungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuDungActionPerformed
        try {
            if (MsgBox.confirm(this, "Bạn có muốn sử dụng điểm không?")) {
                giamGia += Double.parseDouble(txtDiemTVDH.getText()) * 1000;
                taoHoaDon();
                btnSuDung.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSuDungActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdddonDH;
    private javax.swing.JButton btnBackSP;
    private javax.swing.JButton btnBo;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnNewdonDH;
    private javax.swing.JButton btnQuet;
    private javax.swing.JButton btnResest;
    private javax.swing.JButton btnSuDung;
    private javax.swing.JButton btnSuDungDiem;
    private javax.swing.JButton btnTaoHD;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnTimSP;
    private javax.swing.JButton btnXacNhanTheTV;
    private javax.swing.JButton btnXanNhanDH;
    private javax.swing.JComboBox<String> cbbHinhThucThanhToan;
    private javax.swing.JComboBox<String> cbbHinhthucThanhtoanDH;
    private javax.swing.JComboBox<String> cbbLoaiHD;
    private javax.swing.JComboBox<String> cbbUpdateTT;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
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
    private javax.swing.JLabel lblAnh1;
    private javax.swing.JLabel lblAnh2;
    private javax.swing.JLabel lblAnh3;
    private javax.swing.JLabel lblAnh4;
    private javax.swing.JLabel lblDongia;
    private javax.swing.JLabel lblDongia1;
    private javax.swing.JLabel lblDongia2;
    private javax.swing.JLabel lblDongia3;
    private javax.swing.JLabel lblDongsp1;
    private javax.swing.JLabel lblDongsp2;
    private javax.swing.JLabel lblDongsp3;
    private javax.swing.JLabel lblDongsp4;
    private javax.swing.JLabel lblGiamGia;
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
    private javax.swing.JLabel lblTienTraLai;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JPanel pnlDSSP;
    private javax.swing.JPanel pnlSP1;
    private javax.swing.JPanel pnlSP2;
    private javax.swing.JPanel pnlSP3;
    private javax.swing.JPanel pnlSP4;
    private javax.swing.JTabbedPane pnlTab;
    private javax.swing.JTable tblDSHoaDon;
    private javax.swing.JTable tblGiohang;
    private javax.swing.JTextField txtDiemTV;
    private javax.swing.JTextField txtDiemTVDH;
    private javax.swing.JTextArea txtGhichuDH;
    private javax.swing.JTextArea txtGhichuHD;
    private javax.swing.JTextField txtHoTenDH;
    private javax.swing.JTextField txtHoTenTV;
    private javax.swing.JTextField txtMaTVDH;
    private javax.swing.JTextField txtMaTheTV;
    private javax.swing.JTextArea txtNhanhang;
    private javax.swing.JTextField txtPhiDH;
    private javax.swing.JTextField txtPhikhac;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTienKhachTra;
    private javax.swing.JTextField txtTimSP;
    // End of variables declaration//GEN-END:variables
}
