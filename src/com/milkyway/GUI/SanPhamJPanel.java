/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.GUI;

import com.milkyway.DAO.AnhSpDAO;
import com.milkyway.DAO.DonViTinhDAO;
import com.milkyway.DAO.DongSPDAO;
import com.milkyway.DAO.KhoiLuongDAO;
import com.milkyway.DAO.LoaiHangDAO;
import com.milkyway.DAO.NhomHangDAO;
import com.milkyway.DAO.SanPhamDAO;
import com.milkyway.DAO.ThuongHieuDAO;
import com.milkyway.DAO.XuatXuDAO;
import com.milkyway.Model.AnhSP;
import com.milkyway.Model.DonViTinh;
import com.milkyway.Model.DongSP;
import com.milkyway.Model.KhoiLuong;
import com.milkyway.Model.LoaiHang;
import com.milkyway.Model.NhomHang;
import com.milkyway.Model.ThuongHieu;
import com.milkyway.Model.XuatXu;
import com.milkyway.Utils.ImageUtils;
import static com.milkyway.Utils.ImageUtils.resizeImg;
import com.milkyway.Utils.MsgBox;
import com.milkyway.Utils.Validator;
import com.milkyway.Utils.WebcamUtils;
import com.milkyway.subGUI.AnhSPJDialog;
import com.milkyway.subGUI.DonViTinhJDialog;
import com.milkyway.subGUI.DongSPJDialog;
import com.milkyway.subGUI.KhoiLuongJDialog;
import com.milkyway.subGUI.LoaiSPJDialog;
import com.milkyway.subGUI.XuatXuJDialog;
import java.awt.CardLayout;
import java.awt.Color;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author DaiAustinYersin
 */
public class SanPhamJPanel extends javax.swing.JPanel {

    /**
     * Creates new form SanPhamJPanel1
     */
    int rowNH, rowLH, rowTH, rowDSP, rowDVT, rowKL, rowXX, rowASP, rowSPDangKD, rowSPNgungKD = -1;
    String anhSP, anhSP_SP = "";
    static boolean getBarCode = false;
    static String barcode = "";

    NhomHangDAO nhomHangDAO = new NhomHangDAO();
    LoaiHangDAO loaiHangDAO = new LoaiHangDAO();
    List<LoaiHang> listLH = loaiHangDAO.selectAll();
    ThuongHieuDAO thuongHieuDAO = new ThuongHieuDAO();
    List<ThuongHieu> listTH = thuongHieuDAO.selectAll();
    DongSPDAO dongSPDAO = new DongSPDAO();
    List<DongSP> listDSP = dongSPDAO.selectAll();
    DonViTinhDAO donViTinhDAO = new DonViTinhDAO();
    XuatXuDAO xuatXuDAO = new XuatXuDAO();
    KhoiLuongDAO khoiLuongDAO = new KhoiLuongDAO();
    AnhSpDAO anhSpDAO = new AnhSpDAO();
    SanPhamDAO sanPhamDAO = new SanPhamDAO();
    TableRowSorter<TableModel> sortDangKD, sortNgungKinhDoanh;

    public SanPhamJPanel() {
        initComponents();
        init();
    }

    private void init() {
        tbpSanPham.setSelectedIndex(1);
        fillTableDanhMucThuocTinhSP();
        loadComboBoxThuocTinhSP();
        fillTableSanPham();
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String str = txtTimKiem.getText();
                if (str.trim().length() == 0) {
                    sortDangKD.setRowFilter(null);
                } else {
                    //(?i) means case insensitive search
                    sortDangKD.setRowFilter(RowFilter.regexFilter("(?i)" + str));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String str = txtTimKiem.getText();
                if (str.trim().length() == 0) {
                    sortDangKD.setRowFilter(null);
                } else {
                    //(?i) means case insensitive search
                    sortDangKD.setRowFilter(RowFilter.regexFilter("(?i)" + str));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    private void fillTableDanhMucThuocTinhSP() {
        fillTableNhomHang();
        fillTableLoaiHang();
        fillTableThuongHieu();
        fillTableDongSP();
        fillTableDVT();
        fillTableKhoiLuong();
        fillTableXuatXu();
        fillTableAnhSP();
    }

    private void loadComboBoxThuocTinhSP() {
        loadComboBoxLoaiSP();
        loadComboBoxDongSP();
        loadComBoBoxXuatXu();
        loadComBoBoxKhoiLuong();
        loadComBoBoxDonViTinh();
    }

    private void fillTableSanPham() {
        fillTableSanPhamDangKD();
        fillTableSanPhamNgungKD();
    }

    private void updateStatus(int row, JTextField txt, JButton btnThem, JButton... btn) {
        boolean edit = row >= 0;
        txt.setEditable(!edit);
        btnThem.setEnabled(!edit);
        for (int i = 0; i < btn.length; i++) {
            btn[i].setEnabled(edit);
        }
    }

    private boolean checkNull(JTextField... txt) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < txt.length; i++) {
            Validator.isNull(txt[i], txt[i].getName() + " chưa nhập", sb);
        }
        if (sb.length() > 0) {
            MsgBox.alert(this, sb.toString());
            return true;
        }
        return false;
    }

    private boolean checkTrungMa(JTextField txt, JTable tbl) {
        for (int i = 0; i < tbl.getRowCount(); i++) {
            if (txt.getText().equalsIgnoreCase(tbl.getValueAt(i, 0).toString())) {
                txt.setBackground(Color.red);
                return true;
            }
        }
        txt.setBackground(Color.white);
        return false;
    }

    private boolean confirmDelete(JTextField txt) {
        return MsgBox.confirm(this, "Bạn có muốn xóa nhóm hàng mã " + txt.getText());
    }

    private void fillTableNhomHang() {
        DefaultTableModel tableModel = (DefaultTableModel) tblNhomHang.getModel();
        tableModel.setRowCount(0);
        try {
            List<NhomHang> lstNhomHang = nhomHangDAO.selectAll();
            for (NhomHang nh : lstNhomHang) {
                tableModel.addRow(new Object[]{
                    nh.getMaNhom(), nh.getTenNhom(), nh.getGhiChu()
                });
            }
            loadComboBoxNhomHang();
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, e.getMessage());
        }
    }

    private NhomHang getFormNhomHang() {
        NhomHang nh = new NhomHang();
        nh.setMaNhom(txtMaNhomHang.getText());
        nh.setTenNhom(txtTenNhomHang.getText());
        nh.setGhiChu(txtGhiChuNhomHang.getText());
        return nh;
    }

    private void setFormNhomHang(NhomHang nh) {
        txtMaNhomHang.setText(nh.getMaNhom());
        txtTenNhomHang.setText(nh.getTenNhom());
        txtGhiChuNhomHang.setText(nh.getGhiChu());
    }

    private void loadComboBoxNhomHang() {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cbbNhomHang.getModel();
        DefaultComboBoxModel comboBoxModel1 = (DefaultComboBoxModel) cbbNhomHangPhanLoai.getModel();
        comboBoxModel.removeAllElements();
        comboBoxModel1.removeAllElements();
        try {
            List<NhomHang> lst = nhomHangDAO.selectAll();
            for (NhomHang nh : lst) {
                comboBoxModel.addElement(nh);
                comboBoxModel1.addElement(nh);
            }
        } catch (Exception e) {
            MsgBox.alert(this, e.getMessage());
        }
    }

    private void fillTableLoaiHang() {
        DefaultTableModel tableModel = (DefaultTableModel) tblLoaiHang.getModel();
        tableModel.setRowCount(0);
        try {
            NhomHang nh = (NhomHang) cbbNhomHangPhanLoai.getSelectedItem();
            List<LoaiHang> lst = loaiHangDAO.selectByNhomHang(nh.getIDNhomHang() + "");
            for (LoaiHang lh : lst) {
                tableModel.addRow(new Object[]{
                    lh.getMaLoai(), lh.getTenLoai(), nh, lh.getGhiChu()
                });
            }
        } catch (Exception e) {
        }
    }

    private LoaiHang getFormLoaiHang() {
        NhomHang nh = (NhomHang) cbbNhomHang.getSelectedItem();
        LoaiHang lh = new LoaiHang();
        lh.setMaLoai(txtMaLoaiHang.getText());
        lh.setTenLoai(txtTenLoaiHang.getText());
        lh.setIDNhomHang(nh.getIDNhomHang());
        lh.setGhiChu(txtGhiChuLoaiHang.getText());
        return lh;
    }

    private void setFormLoaiHang(LoaiHang lh) {
        txtMaLoaiHang.setText(lh.getMaLoai());
        txtTenLoaiHang.setText(lh.getTenLoai());
        cbbNhomHang.setSelectedIndex(cbbNhomHangPhanLoai.getSelectedIndex());
        txtGhiChuLoaiHang.setText(lh.getGhiChu());
    }

    private void fillTableThuongHieu() {
        DefaultTableModel tableModel = (DefaultTableModel) tblThuongHieu.getModel();
        tableModel.setRowCount(0);
        List<ThuongHieu> lst;
        try {
            if (rdoDangHopTac.isSelected()) {
                lst = thuongHieuDAO.selectByStatus(true);
            } else {
                lst = thuongHieuDAO.selectByStatus(false);
            }
            for (ThuongHieu th : lst) {
                tableModel.addRow(new Object[]{
                    th.getMaTH(), th.getTenTh(), th.isTrangthai(), th.getGhiChu()
                });
            }
            loadComboBoxThuongHieu();
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, e.getMessage());
        }
    }

    private ThuongHieu getFormThuongHieu() {
        ThuongHieu th = new ThuongHieu();
        th.setMaTH(txtMaThuongHieu.getText());
        th.setTenTh(txtTenThuongHieu.getText());
        if (rowTH != -1) {
            th.setTrangthai(Boolean.parseBoolean(tblThuongHieu.getValueAt(rowTH, 2).toString()));
        }
        th.setGhiChu(txtGhiChuThuongHieu.getText());
        return th;
    }

    private void setFormThuongHieu(ThuongHieu nh) {
        txtMaThuongHieu.setText(nh.getMaTH());
        txtTenThuongHieu.setText(nh.getTenTh());
        txtGhiChuThuongHieu.setText(nh.getGhiChu());
    }

    private void loadComboBoxThuongHieu() {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cbbThuongHieu.getModel();
        DefaultComboBoxModel comboBoxModel1 = (DefaultComboBoxModel) cbbThuongHieuPhanLoai.getModel();
        comboBoxModel.removeAllElements();
        comboBoxModel1.removeAllElements();
        try {
            List<ThuongHieu> lst = thuongHieuDAO.selectAll();
            for (ThuongHieu th : lst) {
                comboBoxModel.addElement(th);
                comboBoxModel1.addElement(th);
            }
        } catch (Exception e) {
            MsgBox.alert(this, e.getMessage());
        }
    }

    private void fillTableDongSP() {
        DefaultTableModel tableModel = (DefaultTableModel) tblDongSP.getModel();
        tableModel.setRowCount(0);
        try {
            ThuongHieu th = (ThuongHieu) cbbThuongHieuPhanLoai.getSelectedItem();
            List<DongSP> lst;
            if (rdoDangKinhDoanh.isSelected()) {
                lst = dongSPDAO.selectByThuongHieuAndStatus(String.valueOf(th.getIDThuongHieu()), true);
            } else {
                lst = dongSPDAO.selectByThuongHieuAndStatus(String.valueOf(th.getIDThuongHieu()), false);
            }
            for (DongSP dongSP : lst) {
                tableModel.addRow(new Object[]{
                    dongSP.getMaDongSP(), dongSP.getTenDongSP(), th, dongSP.isTrangThai(), dongSP.getGhiChu()
                });
            }
        } catch (Exception e) {
        }
    }

    private DongSP getFormDongSP() {
        ThuongHieu th = (ThuongHieu) cbbThuongHieu.getSelectedItem();
        DongSP dsp = new DongSP();
        dsp.setMaDongSP(txtMaDongSP.getText().toUpperCase());
        dsp.setTenDongSP(txtTenDongSP.getText());
        dsp.setIDThuongHieu(th.getIDThuongHieu());
        if (rowDSP != -1) {
            dsp.setTrangThai(Boolean.parseBoolean(tblDongSP.getValueAt(rowDSP, 3).toString()));
        }
        dsp.setGhiChu(txtGhiChuDongSP.getText());
        return dsp;
    }

    private void setFormDongSP(DongSP nh) {
        txtMaDongSP.setText(nh.getMaDongSP());
        txtTenDongSP.setText(nh.getTenDongSP());
        cbbThuongHieu.setSelectedIndex(cbbThuongHieuPhanLoai.getSelectedIndex());
        txtGhiChuDongSP.setText(nh.getGhiChu());
    }

    private void fillTableDVT() {
        DefaultTableModel tableModel = (DefaultTableModel) tblDVT.getModel();
        tableModel.setRowCount(0);
        try {
            List<DonViTinh> lst = donViTinhDAO.selectAll();
            for (DonViTinh dvt : lst) {
                tableModel.addRow(new Object[]{
                    dvt.getMaDVT(), dvt.getTenDVT()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DonViTinh getFormDVT() {
        DonViTinh dvt = new DonViTinh();
        dvt.setMaDVT(txtMaDVT.getText().toUpperCase());
        dvt.setTenDVT(txtTenDVT.getText());
        return dvt;
    }

    private void setFormDVT(DonViTinh dvt) {
        txtMaDVT.setText(dvt.getMaDVT());
        txtTenDVT.setText(dvt.getTenDVT());
    }

    private void fillTableXuatXu() {
        DefaultTableModel tableModel = (DefaultTableModel) tblXuatXu.getModel();
        tableModel.setRowCount(0);
        try {
            List<XuatXu> lst = xuatXuDAO.selectAll();
            for (XuatXu xx : lst) {
                tableModel.addRow(new Object[]{
                    xx.getMaQG(), xx.getTenQG()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private XuatXu getFormXuatXu() {
        XuatXu xx = new XuatXu();
        xx.setMaQG(txtMaQG.getText().toUpperCase());
        xx.setTenQG(txtTenQG.getText());
        return xx;
    }

    private void setFormXuatXu(XuatXu dvt) {
        txtMaQG.setText(dvt.getMaQG());
        txtTenQG.setText(dvt.getTenQG());
    }

    private void fillTableKhoiLuong() {
        DefaultTableModel tableModel = (DefaultTableModel) tblKhoiLuong.getModel();
        tableModel.setRowCount(0);
        try {
            List<KhoiLuong> lst = khoiLuongDAO.selectAll();
            for (KhoiLuong kl : lst) {
                tableModel.addRow(new Object[]{
                    kl.getMaKhoiLuong(), kl.getGiaTri()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private KhoiLuong getFormKhoiLuong() {
        KhoiLuong kl = new KhoiLuong();
        kl.setMaKhoiLuong(txtMaKhoiLuong.getText().toUpperCase());
        kl.setGiaTri(txtGiaTri.getText());
        return kl;
    }

    private void setFormKhoiLuong(KhoiLuong kl) {
        txtMaKhoiLuong.setText(kl.getMaKhoiLuong());
        txtGiaTri.setText(kl.getGiaTri());
    }

    private void fillTableAnhSP() {
        DefaultTableModel tableModel = (DefaultTableModel) tblAnhSP.getModel();
        tableModel.setRowCount(0);
        try {
            List<AnhSP> lst = anhSpDAO.selectAll();
            for (AnhSP asp : lst) {
                tableModel.addRow(new Object[]{
                    asp.getMaAnhSP(), asp.getTenAnhSP()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AnhSP getFormAnhSP() {
        AnhSP asp = new AnhSP();
        asp.setMaAnhSP(txtMaAnhSP.getText().toUpperCase());
        asp.setTenAnhSP(txtTenAnhSP.getText());
        return asp;
    }

    private void setFormAnhSP(AnhSP asp) {
        txtMaAnhSP.setText(asp.getMaAnhSP());
        txtTenAnhSP.setText(asp.getTenAnhSP());
        lblAnhSP.setToolTipText(asp.getTenAnhSP());
        ImageIcon icon = ImageUtils.read("SanPham", asp.getTenAnhSP());
        lblAnhSP.setIcon(resizeImg(icon, lblAnhSP));
    }

    private void loadComboBoxLoaiSP() {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cbbLoaiSP.getModel();
        comboBoxModel.removeAllElements();
        try {
            List<LoaiHang> lst = loaiHangDAO.selectAll();
            for (LoaiHang lh : lst) {
                comboBoxModel.addElement(lh.getTenLoai());
            }
        } catch (Exception e) {
            MsgBox.alert(this, e.getMessage());
        }
    }

    private void loadComboBoxDongSP() {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cbbDongSP.getModel();
        comboBoxModel.removeAllElements();
        try {
            List<DongSP> lst = dongSPDAO.selectByStatus(true);
            for (DongSP dsp : lst) {
                comboBoxModel.addElement(dsp.getTenDongSP());
            }
        } catch (Exception e) {
            MsgBox.alert(this, e.getMessage());
        }
    }

    private void loadComBoBoxXuatXu() {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cbbXuatXu.getModel();
        comboBoxModel.removeAllElements();
        try {
            List<XuatXu> lst = xuatXuDAO.selectAll();
            for (XuatXu xx : lst) {
                comboBoxModel.addElement(xx.getTenQG());
            }
        } catch (Exception e) {
            MsgBox.alert(this, e.getMessage());
        }
    }

    private void loadComBoBoxKhoiLuong() {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cbbKhoiLuong.getModel();
        comboBoxModel.removeAllElements();
        try {
            List<KhoiLuong> lst = khoiLuongDAO.selectAll();
            for (KhoiLuong kl : lst) {
                comboBoxModel.addElement(kl.getGiaTri());
            }
        } catch (Exception e) {
            MsgBox.alert(this, e.getMessage());
        }
    }

    private void loadComBoBoxDonViTinh() {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cbbDonViTinh.getModel();
        comboBoxModel.removeAllElements();
        try {
            List<DonViTinh> lst = donViTinhDAO.selectAll();
            for (DonViTinh dvt : lst) {
                comboBoxModel.addElement(dvt.getTenDVT());
            }
        } catch (Exception e) {
            MsgBox.alert(this, e.getMessage());
        }
    }

    private void fillTableSanPhamDangKD() {
        DefaultTableModel tableModel = (DefaultTableModel) tblSanPhamDangKD.getModel();
        sortDangKD = new TableRowSorter<>(tableModel);
        tableModel.setRowCount(0);
        tblSanPhamDangKD.setRowSorter(sortDangKD);
        try {
            List<Object[]> lst = sanPhamDAO.getAllAboutSanPhamDangKD();
            for (Object[] obj : lst) {
                tableModel.addRow(new Object[]{
                    obj[0], obj[1], obj[2], obj[3], obj[4], obj[5], Integer.parseInt(obj[6].toString()), Double.parseDouble(obj[7].toString()), obj[8], obj[9], obj[10], obj[11], obj[12], obj[13], obj[14]
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFormSanPham(Object[] obj) {
        txtMaSP.setText(obj[0].toString());
        txtTenSP.setText(obj[1].toString());
        cbbLoaiSP.setSelectedItem(obj[2]);
        cbbDongSP.setSelectedItem(obj[3]);
        txtNgayXK.setDate(Date.class.cast(obj[4]));
        txtHanSD.setDate(Date.class.cast(obj[5]));
        spnSoLuong.setValue(obj[6]);
        txtDonGia.setText(obj[7].toString());
        cbbXuatXu.setSelectedItem(obj[8]);
        cbbDonViTinh.setSelectedItem(obj[10]);
        cbbKhoiLuong.setSelectedItem(obj[9]);
        txtBarcode.setText(obj[11].toString());
        txtGhiChuSP.setText(obj[12] == null ? "" : obj[12].toString());
        lblAnhSP_SP.setIcon(ImageUtils.resizeImg(ImageUtils.read("SanPham", obj[14].toString()), lblAnhSP_SP));
        lblAnhSP_SP.setToolTipText(obj[14].toString());
    }

    private Object[] getFormSanPham() {
        Object[] obj = new Object[15];
        obj[0] = txtMaSP.getText();
        obj[1] = txtTenSP.getText();
        obj[2] = loaiHangDAO.selectByTenLoai(cbbLoaiSP.getSelectedItem().toString()).getIDLoaiHang();
        obj[3] = dongSPDAO.selectByTenDong(cbbDongSP.getSelectedItem().toString()).getIDDongSP();
        obj[4] = txtNgayXK.getDate();
        obj[5] = txtHanSD.getDate();
        obj[6] = spnSoLuong.getValue();
        obj[7] = txtDonGia.getText();
        obj[8] = xuatXuDAO.selectByTenQG(cbbXuatXu.getSelectedItem().toString()).getIDXuatXu();
        obj[9] = donViTinhDAO.selectByTenDVT(cbbDonViTinh.getSelectedItem().toString()).getIDDonViTinh();
        obj[10] = khoiLuongDAO.selectByGiaTri(cbbKhoiLuong.getSelectedItem().toString()).getIDKhoiLuong();
        obj[11] = txtBarcode.getText();
        obj[12] = txtGhiChuSP.getText();
        obj[14] = anhSpDAO.selectByTenAnh(lblAnhSP_SP.getToolTipText()).getIDAnhSP();
        return obj;
    }

    private void fillTableSanPhamNgungKD() {
        DefaultTableModel tableModel = (DefaultTableModel) tblSanPhamNgungKD.getModel();
        tableModel.setRowCount(0);
        try {
            List<Object[]> lst = sanPhamDAO.getAllAboutSanPhamNgungKD();
            for (Object[] obj : lst) {
                tableModel.addRow(new Object[]{
                    obj[0], obj[1], obj[2], obj[3], obj[4], obj[5], Integer.parseInt(obj[6].toString()), Double.parseDouble(obj[7].toString()), obj[8], obj[9], obj[10], obj[11], obj[12], obj[13], obj[14]
                });
            }
        } catch (Exception e) {
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

        btnGrpNhomLoaiHang = new javax.swing.ButtonGroup();
        btnGrpThuongHieuDongSP = new javax.swing.ButtonGroup();
        btnGrpPhanLoaiThuongHieu = new javax.swing.ButtonGroup();
        btnGrpPhanLoaiDongSP = new javax.swing.ButtonGroup();
        btnGrpSanPham = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        tbpSanPham = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        rdoThuongHieu = new javax.swing.JRadioButton();
        rdoDongSP = new javax.swing.JRadioButton();
        pnlNhomLoaiHang = new javax.swing.JPanel();
        pnlNhomHang = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        txtMaNhomHang = new javax.swing.JTextField();
        txtTenNhomHang = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        txtGhiChuNhomHang = new javax.swing.JTextArea();
        jPanel19 = new javax.swing.JPanel();
        btnThemNhomHang = new javax.swing.JButton();
        btnCapNhatNhomHang = new javax.swing.JButton();
        btnXoaNhomHang = new javax.swing.JButton();
        btnMoiNhomHang = new javax.swing.JButton();
        jScrollPane13 = new javax.swing.JScrollPane();
        tblNhomHang = new javax.swing.JTable();
        pnlLoaiHang = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txtMaLoaiHang = new javax.swing.JTextField();
        txtTenLoaiHang = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        cbbNhomHang = new javax.swing.JComboBox<>();
        jScrollPane14 = new javax.swing.JScrollPane();
        txtGhiChuLoaiHang = new javax.swing.JTextArea();
        jLabel31 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        btnThemLoaiHang = new javax.swing.JButton();
        btnCapNhatLoaiHang = new javax.swing.JButton();
        btnXoaLoaiHang = new javax.swing.JButton();
        btnMoiLoaiHang = new javax.swing.JButton();
        jScrollPane15 = new javax.swing.JScrollPane();
        tblLoaiHang = new javax.swing.JTable();
        jPanel21 = new javax.swing.JPanel();
        cbbNhomHangPhanLoai = new javax.swing.JComboBox<>();
        jPanel18 = new javax.swing.JPanel();
        rdoNhomHang = new javax.swing.JRadioButton();
        rdoLoaihang = new javax.swing.JRadioButton();
        pnlThuongHieuDongSP = new javax.swing.JPanel();
        pnlThuongHieu = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        txtMaThuongHieu = new javax.swing.JTextField();
        txtTenThuongHieu = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jScrollPane16 = new javax.swing.JScrollPane();
        txtGhiChuThuongHieu = new javax.swing.JTextArea();
        jPanel22 = new javax.swing.JPanel();
        btnThemThuongHieu = new javax.swing.JButton();
        btnCapNhatThuongHieu = new javax.swing.JButton();
        btnXoaThuongHieu = new javax.swing.JButton();
        btnMoiThuongHieu = new javax.swing.JButton();
        jScrollPane17 = new javax.swing.JScrollPane();
        tblThuongHieu = new javax.swing.JTable();
        jPanel25 = new javax.swing.JPanel();
        rdoDangHopTac = new javax.swing.JRadioButton();
        rdoNgungHopTac = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        txtTimKiemThuongHieu = new javax.swing.JTextField();
        pnlDongSP = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        txtMaDongSP = new javax.swing.JTextField();
        txtTenDongSP = new javax.swing.JTextField();
        cbbThuongHieu = new javax.swing.JComboBox<>();
        jScrollPane18 = new javax.swing.JScrollPane();
        txtGhiChuDongSP = new javax.swing.JTextArea();
        jPanel23 = new javax.swing.JPanel();
        btnThemDongSP = new javax.swing.JButton();
        btnCapNhatDongSP = new javax.swing.JButton();
        btnXoaDongSP = new javax.swing.JButton();
        btnMoiDongSP = new javax.swing.JButton();
        jScrollPane19 = new javax.swing.JScrollPane();
        tblDongSP = new javax.swing.JTable();
        jPanel24 = new javax.swing.JPanel();
        rdoDangKinhDoanh = new javax.swing.JRadioButton();
        rdoNgungKinhDoanh = new javax.swing.JRadioButton();
        cbbThuongHieuPhanLoai = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        txtTimKiemDongSP = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        lblAnhSP_SP = new javax.swing.JLabel();
        txtMaSP = new javax.swing.JTextField();
        txtTenSP = new javax.swing.JTextField();
        txtNgayXK = new com.toedter.calendar.JDateChooser();
        spnSoLuong = new javax.swing.JSpinner();
        txtDonGia = new javax.swing.JTextField();
        cbbXuatXu = new javax.swing.JComboBox<>();
        pnlTblSanPham = new javax.swing.JPanel();
        srpSanPhamDangKD = new javax.swing.JScrollPane();
        tblSanPhamDangKD = new javax.swing.JTable();
        srpSanPhamNgungKD = new javax.swing.JScrollPane();
        tblSanPhamNgungKD = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        rdoDangKD = new javax.swing.JRadioButton();
        rdoNgungKD = new javax.swing.JRadioButton();
        jLabel39 = new javax.swing.JLabel();
        txtBarcode = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        btnThemSP = new javax.swing.JButton();
        btnCapNhatSP = new javax.swing.JButton();
        btnUpdateStatusSP = new javax.swing.JButton();
        btnChonAnhSP_SP = new javax.swing.JButton();
        btnMoiSP = new javax.swing.JButton();
        cbbDonViTinh = new javax.swing.JComboBox<>();
        cbbKhoiLuong = new javax.swing.JComboBox<>();
        txtHanSD = new com.toedter.calendar.JDateChooser();
        jLabel40 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtGhiChuSP = new javax.swing.JTextArea();
        cbbLoaiSP = new javax.swing.JComboBox<>();
        cbbDongSP = new javax.swing.JComboBox<>();
        btnThemLoaiSPNhanh = new javax.swing.JButton();
        btnThemDongSPNhanh = new javax.swing.JButton();
        btnQuetBarcode = new javax.swing.JButton();
        btnThemXuatXuNhanh = new javax.swing.JButton();
        btnThemDVTNhanh = new javax.swing.JButton();
        btnThemKhoiLuongNhanh = new javax.swing.JButton();
        btnRefreshLoaiSP = new javax.swing.JButton();
        btnThemAnhSPNhanh = new javax.swing.JButton();
        btnRefreshDongSP = new javax.swing.JButton();
        btnRefreshXuatXu = new javax.swing.JButton();
        btnRefreshDVT = new javax.swing.JButton();
        btnRefreshKhoiLuong = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        txtTimKiem = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTenDVT = new javax.swing.JTextField();
        txtMaDVT = new javax.swing.JTextField();
        jPanel26 = new javax.swing.JPanel();
        btnThemDVT = new javax.swing.JButton();
        btnCapNhatDVT = new javax.swing.JButton();
        btnXoaDVT = new javax.swing.JButton();
        btnMoiDVT = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDVT = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtMaKhoiLuong = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        btnThemKhoiLuong = new javax.swing.JButton();
        btnCapNhatKhoiLuong = new javax.swing.JButton();
        btnXoaKhoiLuong = new javax.swing.JButton();
        btnMoiKhoiLuong = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblKhoiLuong = new javax.swing.JTable();
        txtGiaTri = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtMaQG = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        btnThemXuatXu = new javax.swing.JButton();
        btnCapNhatXuatXu = new javax.swing.JButton();
        btnXoaXuatXu = new javax.swing.JButton();
        btnMoiXuatXu = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblXuatXu = new javax.swing.JTable();
        txtTenQG = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtMaAnhSP = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        btnThemAnhSP = new javax.swing.JButton();
        btnCapNhatAnhSP = new javax.swing.JButton();
        btnXoaAnhSP = new javax.swing.JButton();
        btnMoiAnhSP = new javax.swing.JButton();
        btnWebcam = new javax.swing.JButton();
        btnChonAnhSP = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblAnhSP = new javax.swing.JTable();
        txtTenAnhSP = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        lblAnhSP = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(107, 185, 240));

        jPanel2.setBackground(new java.awt.Color(107, 185, 240));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));

        tbpSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tbpSanPhamMouseEntered(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(107, 185, 240));

        jPanel17.setBackground(new java.awt.Color(107, 185, 240));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Phân loại", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        btnGrpThuongHieuDongSP.add(rdoThuongHieu);
        rdoThuongHieu.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        rdoThuongHieu.setForeground(new java.awt.Color(255, 255, 255));
        rdoThuongHieu.setSelected(true);
        rdoThuongHieu.setText("Thương hiệu");
        rdoThuongHieu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoThuongHieuItemStateChanged(evt);
            }
        });

        btnGrpThuongHieuDongSP.add(rdoDongSP);
        rdoDongSP.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        rdoDongSP.setForeground(new java.awt.Color(255, 255, 255));
        rdoDongSP.setText("Dòng sản phẩm");
        rdoDongSP.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoDongSPItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rdoThuongHieu)
                .addGap(18, 18, 18)
                .addComponent(rdoDongSP)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoThuongHieu)
                    .addComponent(rdoDongSP))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlNhomLoaiHang.setBackground(new java.awt.Color(107, 185, 240));
        pnlNhomLoaiHang.setLayout(new java.awt.CardLayout());

        pnlNhomHang.setBackground(new java.awt.Color(107, 185, 240));

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Mã nhóm hàng:");

        txtMaNhomHang.setName("Mã nhóm hàng"); // NOI18N

        txtTenNhomHang.setName("Tên nhóm hàng"); // NOI18N

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Tên nhóm hàng:");

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Ghi chú:");

        txtGhiChuNhomHang.setColumns(20);
        txtGhiChuNhomHang.setRows(5);
        jScrollPane12.setViewportView(txtGhiChuNhomHang);

        jPanel19.setBackground(new java.awt.Color(107, 185, 240));

        btnThemNhomHang.setBackground(new java.awt.Color(153, 255, 153));
        btnThemNhomHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnThemNhomHang.setText("Thêm");
        btnThemNhomHang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemNhomHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNhomHangActionPerformed(evt);
            }
        });
        jPanel19.add(btnThemNhomHang);

        btnCapNhatNhomHang.setBackground(new java.awt.Color(255, 255, 102));
        btnCapNhatNhomHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Edit.png"))); // NOI18N
        btnCapNhatNhomHang.setText("Cập nhật");
        btnCapNhatNhomHang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCapNhatNhomHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatNhomHangActionPerformed(evt);
            }
        });
        jPanel19.add(btnCapNhatNhomHang);

        btnXoaNhomHang.setBackground(new java.awt.Color(255, 153, 153));
        btnXoaNhomHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Delete.png"))); // NOI18N
        btnXoaNhomHang.setText("Xoá");
        btnXoaNhomHang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoaNhomHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaNhomHangActionPerformed(evt);
            }
        });
        jPanel19.add(btnXoaNhomHang);

        btnMoiNhomHang.setBackground(new java.awt.Color(204, 204, 204));
        btnMoiNhomHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Document.png"))); // NOI18N
        btnMoiNhomHang.setText("Mới");
        btnMoiNhomHang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMoiNhomHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiNhomHangActionPerformed(evt);
            }
        });
        jPanel19.add(btnMoiNhomHang);

        tblNhomHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã nhóm hàng", "Tên nhóm hàng", "Ghi chú"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNhomHang.setRowHeight(25);
        tblNhomHang.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblNhomHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNhomHangMouseClicked(evt);
            }
        });
        jScrollPane13.setViewportView(tblNhomHang);

        javax.swing.GroupLayout pnlNhomHangLayout = new javax.swing.GroupLayout(pnlNhomHang);
        pnlNhomHang.setLayout(pnlNhomHangLayout);
        pnlNhomHangLayout.setHorizontalGroup(
            pnlNhomHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNhomHangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlNhomHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
                    .addGroup(pnlNhomHangLayout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addGap(18, 18, 18)
                        .addComponent(txtMaNhomHang, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlNhomHangLayout.createSequentialGroup()
                        .addGroup(pnlNhomHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlNhomHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTenNhomHang, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                            .addComponent(jScrollPane12))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlNhomHangLayout.setVerticalGroup(
            pnlNhomHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlNhomHangLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlNhomHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlNhomHangLayout.createSequentialGroup()
                        .addGroup(pnlNhomHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaNhomHang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25))
                        .addGap(18, 18, 18)
                        .addGroup(pnlNhomHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(txtTenNhomHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlNhomHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(587, 587, 587))
        );

        pnlNhomHangLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtMaNhomHang, txtTenNhomHang});

        pnlNhomLoaiHang.add(pnlNhomHang, "card2");

        pnlLoaiHang.setBackground(new java.awt.Color(107, 185, 240));

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Tên loại hàng:");

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Mã loại hàng:");

        txtMaLoaiHang.setName("Mã loại hàng"); // NOI18N

        txtTenLoaiHang.setName("Tên loại hàng"); // NOI18N

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Nhóm hàng:");

        txtGhiChuLoaiHang.setColumns(20);
        txtGhiChuLoaiHang.setRows(5);
        jScrollPane14.setViewportView(txtGhiChuLoaiHang);

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Ghi chú:");

        jPanel20.setBackground(new java.awt.Color(107, 185, 240));

        btnThemLoaiHang.setBackground(new java.awt.Color(153, 255, 153));
        btnThemLoaiHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnThemLoaiHang.setText("Thêm");
        btnThemLoaiHang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemLoaiHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemLoaiHangActionPerformed(evt);
            }
        });
        jPanel20.add(btnThemLoaiHang);

        btnCapNhatLoaiHang.setBackground(new java.awt.Color(255, 255, 102));
        btnCapNhatLoaiHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Edit.png"))); // NOI18N
        btnCapNhatLoaiHang.setText("Cập nhật");
        btnCapNhatLoaiHang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCapNhatLoaiHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatLoaiHangActionPerformed(evt);
            }
        });
        jPanel20.add(btnCapNhatLoaiHang);

        btnXoaLoaiHang.setBackground(new java.awt.Color(255, 153, 153));
        btnXoaLoaiHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Delete.png"))); // NOI18N
        btnXoaLoaiHang.setText("Xoá");
        btnXoaLoaiHang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoaLoaiHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaLoaiHangActionPerformed(evt);
            }
        });
        jPanel20.add(btnXoaLoaiHang);

        btnMoiLoaiHang.setBackground(new java.awt.Color(204, 204, 204));
        btnMoiLoaiHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Document.png"))); // NOI18N
        btnMoiLoaiHang.setText("Mới");
        btnMoiLoaiHang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMoiLoaiHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiLoaiHangActionPerformed(evt);
            }
        });
        jPanel20.add(btnMoiLoaiHang);

        tblLoaiHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã loại hàng", "Tên loại hàng", "Nhóm hàng", "Ghi chú"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblLoaiHang.setRowHeight(25);
        tblLoaiHang.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblLoaiHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLoaiHangMouseClicked(evt);
            }
        });
        jScrollPane15.setViewportView(tblLoaiHang);

        jPanel21.setBackground(new java.awt.Color(107, 185, 240));
        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Phân loại", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        cbbNhomHangPhanLoai.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbNhomHangPhanLoaiItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbbNhomHangPhanLoai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbbNhomHangPhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlLoaiHangLayout = new javax.swing.GroupLayout(pnlLoaiHang);
        pnlLoaiHang.setLayout(pnlLoaiHangLayout);
        pnlLoaiHangLayout.setHorizontalGroup(
            pnlLoaiHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoaiHangLayout.createSequentialGroup()
                .addGroup(pnlLoaiHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlLoaiHangLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlLoaiHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlLoaiHangLayout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addGap(18, 18, 18)
                                .addComponent(txtMaLoaiHang, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlLoaiHangLayout.createSequentialGroup()
                                .addGroup(pnlLoaiHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel30)
                                    .addComponent(jLabel31))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlLoaiHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTenLoaiHang, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                                    .addComponent(cbbNhomHang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane14))))))
                .addGap(18, 18, 18)
                .addGroup(pnlLoaiHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        pnlLoaiHangLayout.setVerticalGroup(
            pnlLoaiHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoaiHangLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlLoaiHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLoaiHangLayout.createSequentialGroup()
                        .addGroup(pnlLoaiHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaLoaiHang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29))
                        .addGap(18, 18, 18)
                        .addGroup(pnlLoaiHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(txtTenLoaiHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(pnlLoaiHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLoaiHangLayout.createSequentialGroup()
                        .addGroup(pnlLoaiHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(cbbNhomHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(pnlLoaiHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31)
                            .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(545, 545, 545))
        );

        pnlLoaiHangLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cbbNhomHang, txtMaLoaiHang, txtTenLoaiHang});

        pnlNhomLoaiHang.add(pnlLoaiHang, "card3");

        jPanel18.setBackground(new java.awt.Color(107, 185, 240));
        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Phân loại", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        btnGrpNhomLoaiHang.add(rdoNhomHang);
        rdoNhomHang.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        rdoNhomHang.setForeground(new java.awt.Color(255, 255, 255));
        rdoNhomHang.setSelected(true);
        rdoNhomHang.setText("Nhóm hàng");
        rdoNhomHang.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoNhomHangItemStateChanged(evt);
            }
        });

        btnGrpNhomLoaiHang.add(rdoLoaihang);
        rdoLoaihang.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        rdoLoaihang.setForeground(new java.awt.Color(255, 255, 255));
        rdoLoaihang.setText("Loại hàng");
        rdoLoaihang.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoLoaihangItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rdoNhomHang)
                .addGap(18, 18, 18)
                .addComponent(rdoLoaihang)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoNhomHang)
                    .addComponent(rdoLoaihang))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlThuongHieuDongSP.setBackground(new java.awt.Color(107, 185, 240));
        pnlThuongHieuDongSP.setLayout(new java.awt.CardLayout());

        pnlThuongHieu.setBackground(new java.awt.Color(107, 185, 240));

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Mã thương hiệu:");

        txtMaThuongHieu.setName("Mã thương hiệu"); // NOI18N

        txtTenThuongHieu.setName("Tên thương hiệu"); // NOI18N

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Tên thương hiệu:");

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Ghi chú:");

        txtGhiChuThuongHieu.setColumns(20);
        txtGhiChuThuongHieu.setRows(5);
        jScrollPane16.setViewportView(txtGhiChuThuongHieu);

        jPanel22.setBackground(new java.awt.Color(107, 185, 240));

        btnThemThuongHieu.setBackground(new java.awt.Color(153, 255, 153));
        btnThemThuongHieu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnThemThuongHieu.setText("Thêm");
        btnThemThuongHieu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemThuongHieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemThuongHieuActionPerformed(evt);
            }
        });
        jPanel22.add(btnThemThuongHieu);

        btnCapNhatThuongHieu.setBackground(new java.awt.Color(255, 255, 102));
        btnCapNhatThuongHieu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Edit.png"))); // NOI18N
        btnCapNhatThuongHieu.setText("Cập nhật");
        btnCapNhatThuongHieu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCapNhatThuongHieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatThuongHieuActionPerformed(evt);
            }
        });
        jPanel22.add(btnCapNhatThuongHieu);

        btnXoaThuongHieu.setBackground(new java.awt.Color(255, 153, 153));
        btnXoaThuongHieu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Delete.png"))); // NOI18N
        btnXoaThuongHieu.setText("Xoá");
        btnXoaThuongHieu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoaThuongHieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaThuongHieuActionPerformed(evt);
            }
        });
        jPanel22.add(btnXoaThuongHieu);

        btnMoiThuongHieu.setBackground(new java.awt.Color(204, 204, 204));
        btnMoiThuongHieu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Document.png"))); // NOI18N
        btnMoiThuongHieu.setText("Mới");
        btnMoiThuongHieu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMoiThuongHieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiThuongHieuActionPerformed(evt);
            }
        });
        jPanel22.add(btnMoiThuongHieu);

        tblThuongHieu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã thương hiệu", "Tên thương hiệu", "Còn hợp tác", "Ghi chú"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblThuongHieu.setRowHeight(25);
        tblThuongHieu.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblThuongHieu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblThuongHieuMouseClicked(evt);
            }
        });
        jScrollPane17.setViewportView(tblThuongHieu);

        jPanel25.setBackground(new java.awt.Color(107, 185, 240));
        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Phân loại", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        btnGrpPhanLoaiThuongHieu.add(rdoDangHopTac);
        rdoDangHopTac.setForeground(new java.awt.Color(255, 255, 255));
        rdoDangHopTac.setSelected(true);
        rdoDangHopTac.setText("Đang hợp tác");
        rdoDangHopTac.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoDangHopTacItemStateChanged(evt);
            }
        });

        btnGrpPhanLoaiThuongHieu.add(rdoNgungHopTac);
        rdoNgungHopTac.setForeground(new java.awt.Color(255, 255, 255));
        rdoNgungHopTac.setText("Ngừng hợp tác");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rdoDangHopTac)
                .addGap(18, 18, 18)
                .addComponent(rdoNgungHopTac)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoDangHopTac)
                    .addComponent(rdoNgungHopTac))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(107, 185, 240));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 15), new java.awt.Color(255, 255, 255))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTimKiemThuongHieu)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTimKiemThuongHieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlThuongHieuLayout = new javax.swing.GroupLayout(pnlThuongHieu);
        pnlThuongHieu.setLayout(pnlThuongHieuLayout);
        pnlThuongHieuLayout.setHorizontalGroup(
            pnlThuongHieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThuongHieuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlThuongHieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                    .addGroup(pnlThuongHieuLayout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addGap(18, 18, 18)
                        .addComponent(txtMaThuongHieu, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlThuongHieuLayout.createSequentialGroup()
                        .addGroup(pnlThuongHieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33)
                            .addComponent(jLabel34))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlThuongHieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTenThuongHieu, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                            .addComponent(jScrollPane16))))
                .addGap(18, 18, 18)
                .addGroup(pnlThuongHieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlThuongHieuLayout.createSequentialGroup()
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 901, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(198, Short.MAX_VALUE))
        );
        pnlThuongHieuLayout.setVerticalGroup(
            pnlThuongHieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThuongHieuLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlThuongHieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlThuongHieuLayout.createSequentialGroup()
                        .addGroup(pnlThuongHieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaThuongHieu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32))
                        .addGap(18, 18, 18)
                        .addGroup(pnlThuongHieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(txtTenThuongHieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(pnlThuongHieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThuongHieuLayout.createSequentialGroup()
                        .addGroup(pnlThuongHieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34)
                            .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(578, 578, 578))
        );

        pnlThuongHieuLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtMaThuongHieu, txtTenThuongHieu});

        pnlThuongHieuDongSP.add(pnlThuongHieu, "card2");

        pnlDongSP.setBackground(new java.awt.Color(107, 185, 240));

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Ghi chú:");

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Thương hiệu:");

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Tên dòng sản phẩm:");

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Mã dòng sản phẩm:");

        txtMaDongSP.setName("Mã dòng sản phẩm"); // NOI18N
        txtMaDongSP.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaDongSPFocusLost(evt);
            }
        });

        txtTenDongSP.setName("Tên dòng sản phẩm"); // NOI18N

        txtGhiChuDongSP.setColumns(20);
        txtGhiChuDongSP.setRows(5);
        jScrollPane18.setViewportView(txtGhiChuDongSP);

        jPanel23.setBackground(new java.awt.Color(107, 185, 240));

        btnThemDongSP.setBackground(new java.awt.Color(153, 255, 153));
        btnThemDongSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnThemDongSP.setText("Thêm");
        btnThemDongSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemDongSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemDongSPActionPerformed(evt);
            }
        });
        jPanel23.add(btnThemDongSP);

        btnCapNhatDongSP.setBackground(new java.awt.Color(255, 255, 102));
        btnCapNhatDongSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Edit.png"))); // NOI18N
        btnCapNhatDongSP.setText("Cập nhật");
        btnCapNhatDongSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCapNhatDongSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatDongSPActionPerformed(evt);
            }
        });
        jPanel23.add(btnCapNhatDongSP);

        btnXoaDongSP.setBackground(new java.awt.Color(255, 153, 153));
        btnXoaDongSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Delete.png"))); // NOI18N
        btnXoaDongSP.setText("Xoá");
        btnXoaDongSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoaDongSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaDongSPActionPerformed(evt);
            }
        });
        jPanel23.add(btnXoaDongSP);

        btnMoiDongSP.setBackground(new java.awt.Color(204, 204, 204));
        btnMoiDongSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Document.png"))); // NOI18N
        btnMoiDongSP.setText("Mới");
        btnMoiDongSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMoiDongSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiDongSPActionPerformed(evt);
            }
        });
        jPanel23.add(btnMoiDongSP);

        tblDongSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã dòng sản phẩm", "Tên dòng sản phẩm", "Thương hiệu", "Còn kinh doanh", "Ghi chú"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDongSP.setRowHeight(25);
        tblDongSP.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblDongSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDongSPMouseClicked(evt);
            }
        });
        jScrollPane19.setViewportView(tblDongSP);

        jPanel24.setBackground(new java.awt.Color(107, 185, 240));
        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Phân loại", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        btnGrpPhanLoaiDongSP.add(rdoDangKinhDoanh);
        rdoDangKinhDoanh.setForeground(new java.awt.Color(255, 255, 255));
        rdoDangKinhDoanh.setSelected(true);
        rdoDangKinhDoanh.setText("Đang kinh doanh");
        rdoDangKinhDoanh.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoDangKinhDoanhItemStateChanged(evt);
            }
        });

        btnGrpPhanLoaiDongSP.add(rdoNgungKinhDoanh);
        rdoNgungKinhDoanh.setForeground(new java.awt.Color(255, 255, 255));
        rdoNgungKinhDoanh.setText("Ngừng kinh doanh");

        cbbThuongHieuPhanLoai.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbThuongHieuPhanLoaiItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rdoDangKinhDoanh)
                .addGap(18, 18, 18)
                .addComponent(rdoNgungKinhDoanh)
                .addGap(18, 18, 18)
                .addComponent(cbbThuongHieuPhanLoai, 0, 185, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoDangKinhDoanh)
                    .addComponent(rdoNgungKinhDoanh)
                    .addComponent(cbbThuongHieuPhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(107, 185, 240));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTimKiemDongSP, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTimKiemDongSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlDongSPLayout = new javax.swing.GroupLayout(pnlDongSP);
        pnlDongSP.setLayout(pnlDongSPLayout);
        pnlDongSPLayout.setHorizontalGroup(
            pnlDongSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDongSPLayout.createSequentialGroup()
                .addGroup(pnlDongSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlDongSPLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlDongSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlDongSPLayout.createSequentialGroup()
                                .addComponent(jLabel38)
                                .addGap(18, 18, 18)
                                .addComponent(txtMaDongSP, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlDongSPLayout.createSequentialGroup()
                                .addGroup(pnlDongSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel37)
                                    .addComponent(jLabel36)
                                    .addComponent(jLabel35))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlDongSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTenDongSP, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                                    .addComponent(cbbThuongHieu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane18)))))
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(pnlDongSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlDongSPLayout.createSequentialGroup()
                        .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane19))
                .addContainerGap(204, Short.MAX_VALUE))
        );
        pnlDongSPLayout.setVerticalGroup(
            pnlDongSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDongSPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlDongSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlDongSPLayout.createSequentialGroup()
                        .addGroup(pnlDongSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaDongSP, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38))
                        .addGap(18, 18, 18)
                        .addGroup(pnlDongSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37)
                            .addComponent(txtTenDongSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(pnlDongSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDongSPLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(pnlDongSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(cbbThuongHieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(pnlDongSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35)
                            .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(545, 545, 545))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDongSPLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(455, 455, 455))))
        );

        pnlDongSPLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cbbThuongHieu, txtMaDongSP, txtTenDongSP});

        pnlThuongHieuDongSP.add(pnlDongSP, "card3");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlNhomLoaiHang, javax.swing.GroupLayout.PREFERRED_SIZE, 1124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(165, 165, 165))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlThuongHieuDongSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(1340, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlNhomLoaiHang, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlThuongHieuDongSP, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(23, 23, 23)
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(753, Short.MAX_VALUE)))
        );

        tbpSanPham.addTab("Danh mục sản phẩm", jPanel7);

        jPanel5.setBackground(new java.awt.Color(107, 185, 240));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Mã sản phẩm:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Loại sản phẩm:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Dòng sản phẩm:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Tên sản phẩm:");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Ngày xuất kho:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Hạn sử dụng:");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Số lượng tồn:");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Đơn giá:");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Xuất xứ:");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Khối lượng:");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Đơn vị tính:");

        jPanel13.setBackground(new java.awt.Color(107, 185, 240));
        jPanel13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));

        lblAnhSP_SP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAnhSP_SP.setToolTipText("");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnhSP_SP, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnhSP_SP, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtMaSP.setName("Mã sản phẩm"); // NOI18N

        txtTenSP.setName("Tên sản phẩm"); // NOI18N

        txtNgayXK.setEnabled(false);

        txtDonGia.setName("Đơn giá"); // NOI18N

        pnlTblSanPham.setBackground(new java.awt.Color(107, 185, 240));
        pnlTblSanPham.setLayout(new java.awt.CardLayout());

        tblSanPhamDangKD.setAutoCreateRowSorter(true);
        tblSanPhamDangKD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null,  new Boolean(false), null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Loại hàng", "Dòng sản phẩm", "Ngày xuất kho", "Hạn sử dụng", "Số lượng tồn", "Đơn giá", "Xuất xứ", "Khối lượng", "Đơn vị tính", "Barcode", "Ghi chú", "Đang kinh doanh", "Ảnh"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSanPhamDangKD.setRowHeight(25);
        tblSanPhamDangKD.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblSanPhamDangKD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamDangKDMouseClicked(evt);
            }
        });
        srpSanPhamDangKD.setViewportView(tblSanPhamDangKD);

        pnlTblSanPham.add(srpSanPhamDangKD, "card2");

        tblSanPhamNgungKD.setAutoCreateRowSorter(true);
        tblSanPhamNgungKD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null,  new Boolean(false), null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Loại hàng", "Dòng sản phẩm", "Ngày xuất kho", "Hạn sử dụng", "Số lượng tồn", "Đơn giá", "Xuất xứ", "Khối lượng", "Đơn vị tính", "Barcode", "Ghi chú", "Đang kinh doanh", "Ảnh"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSanPhamNgungKD.setRowHeight(25);
        tblSanPhamNgungKD.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblSanPhamNgungKD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamNgungKDMouseClicked(evt);
            }
        });
        srpSanPhamNgungKD.setViewportView(tblSanPhamNgungKD);

        pnlTblSanPham.add(srpSanPhamNgungKD, "card2");

        jPanel15.setBackground(new java.awt.Color(107, 185, 240));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Phân loại", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        btnGrpSanPham.add(rdoDangKD);
        rdoDangKD.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        rdoDangKD.setForeground(new java.awt.Color(255, 255, 255));
        rdoDangKD.setSelected(true);
        rdoDangKD.setText("Đang kinh doanh");
        rdoDangKD.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoDangKDItemStateChanged(evt);
            }
        });

        btnGrpSanPham.add(rdoNgungKD);
        rdoNgungKD.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        rdoNgungKD.setForeground(new java.awt.Color(255, 255, 255));
        rdoNgungKD.setText("Ngừng kinh doanh");
        rdoNgungKD.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoNgungKDItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rdoDangKD)
                .addGap(18, 18, 18)
                .addComponent(rdoNgungKD)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoDangKD)
                    .addComponent(rdoNgungKD))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Barcode:");

        txtBarcode.setName("Barcode"); // NOI18N

        jPanel14.setBackground(new java.awt.Color(107, 185, 240));

        btnThemSP.setBackground(new java.awt.Color(153, 255, 153));
        btnThemSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnThemSP.setText("Thêm");
        btnThemSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSPActionPerformed(evt);
            }
        });
        jPanel14.add(btnThemSP);

        btnCapNhatSP.setBackground(new java.awt.Color(255, 255, 102));
        btnCapNhatSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Edit.png"))); // NOI18N
        btnCapNhatSP.setText("Cập nhật");
        btnCapNhatSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCapNhatSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatSPActionPerformed(evt);
            }
        });
        jPanel14.add(btnCapNhatSP);

        btnUpdateStatusSP.setBackground(new java.awt.Color(153, 255, 255));
        btnUpdateStatusSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Save.png"))); // NOI18N
        btnUpdateStatusSP.setText("Cập nhật trạng thái");
        btnUpdateStatusSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdateStatusSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateStatusSPActionPerformed(evt);
            }
        });
        jPanel14.add(btnUpdateStatusSP);

        btnChonAnhSP_SP.setBackground(new java.awt.Color(153, 153, 255));
        btnChonAnhSP_SP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Upload.png"))); // NOI18N
        btnChonAnhSP_SP.setText("Chọn ảnh");
        btnChonAnhSP_SP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChonAnhSP_SP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonAnhSP_SPActionPerformed(evt);
            }
        });
        jPanel14.add(btnChonAnhSP_SP);

        btnMoiSP.setBackground(new java.awt.Color(204, 204, 204));
        btnMoiSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Document.png"))); // NOI18N
        btnMoiSP.setText("Mới");
        btnMoiSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMoiSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiSPActionPerformed(evt);
            }
        });
        jPanel14.add(btnMoiSP);

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Ghi chú:");

        txtGhiChuSP.setColumns(20);
        txtGhiChuSP.setRows(5);
        jScrollPane5.setViewportView(txtGhiChuSP);

        cbbLoaiSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbbDongSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnThemLoaiSPNhanh.setBackground(new java.awt.Color(255, 255, 255));
        btnThemLoaiSPNhanh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnThemLoaiSPNhanh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemLoaiSPNhanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemLoaiSPNhanhActionPerformed(evt);
            }
        });

        btnThemDongSPNhanh.setBackground(new java.awt.Color(255, 255, 255));
        btnThemDongSPNhanh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnThemDongSPNhanh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemDongSPNhanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemDongSPNhanhActionPerformed(evt);
            }
        });

        btnQuetBarcode.setBackground(new java.awt.Color(255, 255, 255));
        btnQuetBarcode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/barcode.png"))); // NOI18N
        btnQuetBarcode.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnQuetBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuetBarcodeActionPerformed(evt);
            }
        });

        btnThemXuatXuNhanh.setBackground(new java.awt.Color(255, 255, 255));
        btnThemXuatXuNhanh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnThemXuatXuNhanh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemXuatXuNhanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemXuatXuNhanhActionPerformed(evt);
            }
        });

        btnThemDVTNhanh.setBackground(new java.awt.Color(255, 255, 255));
        btnThemDVTNhanh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnThemDVTNhanh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemDVTNhanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemDVTNhanhActionPerformed(evt);
            }
        });

        btnThemKhoiLuongNhanh.setBackground(new java.awt.Color(255, 255, 255));
        btnThemKhoiLuongNhanh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnThemKhoiLuongNhanh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemKhoiLuongNhanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemKhoiLuongNhanhActionPerformed(evt);
            }
        });

        btnRefreshLoaiSP.setBackground(new java.awt.Color(255, 255, 255));
        btnRefreshLoaiSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Refresh-icon.png"))); // NOI18N
        btnRefreshLoaiSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefreshLoaiSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshLoaiSPActionPerformed(evt);
            }
        });

        btnThemAnhSPNhanh.setBackground(new java.awt.Color(255, 255, 255));
        btnThemAnhSPNhanh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnThemAnhSPNhanh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemAnhSPNhanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemAnhSPNhanhActionPerformed(evt);
            }
        });

        btnRefreshDongSP.setBackground(new java.awt.Color(255, 255, 255));
        btnRefreshDongSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Refresh-icon.png"))); // NOI18N
        btnRefreshDongSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefreshDongSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshDongSPActionPerformed(evt);
            }
        });

        btnRefreshXuatXu.setBackground(new java.awt.Color(255, 255, 255));
        btnRefreshXuatXu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Refresh-icon.png"))); // NOI18N
        btnRefreshXuatXu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefreshXuatXu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshXuatXuActionPerformed(evt);
            }
        });

        btnRefreshDVT.setBackground(new java.awt.Color(255, 255, 255));
        btnRefreshDVT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Refresh-icon.png"))); // NOI18N
        btnRefreshDVT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefreshDVT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshDVTActionPerformed(evt);
            }
        });

        btnRefreshKhoiLuong.setBackground(new java.awt.Color(255, 255, 255));
        btnRefreshKhoiLuong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Refresh-icon.png"))); // NOI18N
        btnRefreshKhoiLuong.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefreshKhoiLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshKhoiLuongActionPerformed(evt);
            }
        });

        jPanel16.setBackground(new java.awt.Color(107, 185, 240));
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlTblSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel13)
                                            .addComponent(jLabel14)
                                            .addComponent(jLabel12)
                                            .addComponent(jLabel11)
                                            .addComponent(jLabel15)
                                            .addComponent(jLabel16))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtMaSP, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                                            .addComponent(txtTenSP, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                                            .addComponent(txtNgayXK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtHanSD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cbbLoaiSP, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cbbDongSP, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addComponent(btnThemLoaiSPNhanh)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnRefreshLoaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addComponent(btnThemDongSPNhanh)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnRefreshDongSP, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(57, 57, 57)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel17)
                                            .addComponent(jLabel18)
                                            .addComponent(jLabel19)
                                            .addComponent(jLabel20)
                                            .addComponent(jLabel39)
                                            .addComponent(jLabel21)
                                            .addComponent(jLabel40)))
                                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(14, 14, 14)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbbDonViTinh, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtBarcode)
                                    .addComponent(cbbXuatXu, 0, 378, Short.MAX_VALUE)
                                    .addComponent(txtDonGia)
                                    .addComponent(cbbKhoiLuong, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(spnSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(jScrollPane5))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnQuetBarcode)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(btnThemDVTNhanh)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnRefreshDVT, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(btnThemXuatXuNhanh)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnRefreshXuatXu, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(btnThemKhoiLuongNhanh)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnRefreshKhoiLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(82, 82, 82))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(btnThemAnhSPNhanh)
                                .addGap(131, 131, 131)))))
                .addContainerGap())
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtMaSP, txtTenSP});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)
                            .addComponent(spnSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18)
                            .addComponent(txtDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel12)
                                .addComponent(cbbXuatXu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel19)
                                .addComponent(cbbLoaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnThemLoaiSPNhanh)
                            .addComponent(btnThemXuatXuNhanh)
                            .addComponent(btnRefreshLoaiSP)
                            .addComponent(btnRefreshXuatXu))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnThemDongSPNhanh)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel21)
                                        .addComponent(cbbDonViTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel13)
                                        .addComponent(cbbDongSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnThemDVTNhanh)
                                    .addComponent(btnRefreshDVT))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel15)
                                            .addComponent(txtNgayXK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel20)
                                                .addComponent(cbbKhoiLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(24, 24, 24)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel16)
                                            .addComponent(txtHanSD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel39)
                                                .addComponent(txtBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(btnRefreshKhoiLuong)
                                        .addGap(5, 5, 5)
                                        .addComponent(btnQuetBarcode))
                                    .addComponent(btnThemKhoiLuongNhanh)))
                            .addComponent(btnRefreshDongSP))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel40)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemAnhSPNhanh)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlTblSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
        );

        tbpSanPham.addTab("Sản phẩm", jPanel5);

        jPanel8.setBackground(new java.awt.Color(107, 185, 240));

        jPanel6.setBackground(new java.awt.Color(107, 185, 240));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Đơn vị tính", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Mã đơn vị tính:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Tên đơn vị tính:");

        txtTenDVT.setName("Tên đơn vị tính"); // NOI18N

        txtMaDVT.setName("Mã đơn vị tính"); // NOI18N

        jPanel26.setBackground(new java.awt.Color(107, 185, 240));

        btnThemDVT.setBackground(new java.awt.Color(153, 255, 153));
        btnThemDVT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnThemDVT.setText("Thêm");
        btnThemDVT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemDVT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemDVTActionPerformed(evt);
            }
        });
        jPanel26.add(btnThemDVT);

        btnCapNhatDVT.setBackground(new java.awt.Color(255, 255, 102));
        btnCapNhatDVT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Edit.png"))); // NOI18N
        btnCapNhatDVT.setText("Cập nhật");
        btnCapNhatDVT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCapNhatDVT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatDVTActionPerformed(evt);
            }
        });
        jPanel26.add(btnCapNhatDVT);

        btnXoaDVT.setBackground(new java.awt.Color(255, 153, 153));
        btnXoaDVT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Delete.png"))); // NOI18N
        btnXoaDVT.setText("Xoá");
        btnXoaDVT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoaDVT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaDVTActionPerformed(evt);
            }
        });
        jPanel26.add(btnXoaDVT);

        btnMoiDVT.setBackground(new java.awt.Color(204, 204, 204));
        btnMoiDVT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Document.png"))); // NOI18N
        btnMoiDVT.setText("Mới");
        btnMoiDVT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMoiDVT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiDVTActionPerformed(evt);
            }
        });
        jPanel26.add(btnMoiDVT);

        tblDVT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Mã đơn vị tính", "Tên đơn vị tính"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDVT.setRowHeight(25);
        tblDVT.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblDVT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDVTMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDVT);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaDVT, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenDVT, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtMaDVT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtTenDVT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(107, 185, 240));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Khối lượng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Mã khối lượng:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Giá trị:");

        txtMaKhoiLuong.setName("Mã khối lượng"); // NOI18N

        jPanel27.setBackground(new java.awt.Color(107, 185, 240));

        btnThemKhoiLuong.setBackground(new java.awt.Color(153, 255, 153));
        btnThemKhoiLuong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnThemKhoiLuong.setText("Thêm");
        btnThemKhoiLuong.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemKhoiLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemKhoiLuongActionPerformed(evt);
            }
        });
        jPanel27.add(btnThemKhoiLuong);

        btnCapNhatKhoiLuong.setBackground(new java.awt.Color(255, 255, 102));
        btnCapNhatKhoiLuong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Edit.png"))); // NOI18N
        btnCapNhatKhoiLuong.setText("Cập nhật");
        btnCapNhatKhoiLuong.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCapNhatKhoiLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatKhoiLuongActionPerformed(evt);
            }
        });
        jPanel27.add(btnCapNhatKhoiLuong);

        btnXoaKhoiLuong.setBackground(new java.awt.Color(255, 153, 153));
        btnXoaKhoiLuong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Delete.png"))); // NOI18N
        btnXoaKhoiLuong.setText("Xoá");
        btnXoaKhoiLuong.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoaKhoiLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaKhoiLuongActionPerformed(evt);
            }
        });
        jPanel27.add(btnXoaKhoiLuong);

        btnMoiKhoiLuong.setBackground(new java.awt.Color(204, 204, 204));
        btnMoiKhoiLuong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Document.png"))); // NOI18N
        btnMoiKhoiLuong.setText("Mới");
        btnMoiKhoiLuong.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMoiKhoiLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiKhoiLuongActionPerformed(evt);
            }
        });
        jPanel27.add(btnMoiKhoiLuong);

        tblKhoiLuong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Mã khối lượng", "Giá trị"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKhoiLuong.setRowHeight(25);
        tblKhoiLuong.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblKhoiLuong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKhoiLuongMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblKhoiLuong);

        txtGiaTri.setName("Giá trị"); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMaKhoiLuong, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                            .addComponent(txtGiaTri)))
                    .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtMaKhoiLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtGiaTri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel10.setBackground(new java.awt.Color(107, 185, 240));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Xuất xứ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Mã quốc gia:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Tên quốc gia:");

        txtMaQG.setName("Mã quốc gia"); // NOI18N

        jPanel28.setBackground(new java.awt.Color(107, 185, 240));

        btnThemXuatXu.setBackground(new java.awt.Color(153, 255, 153));
        btnThemXuatXu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnThemXuatXu.setText("Thêm");
        btnThemXuatXu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemXuatXu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemXuatXuActionPerformed(evt);
            }
        });
        jPanel28.add(btnThemXuatXu);

        btnCapNhatXuatXu.setBackground(new java.awt.Color(255, 255, 102));
        btnCapNhatXuatXu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Edit.png"))); // NOI18N
        btnCapNhatXuatXu.setText("Cập nhật");
        btnCapNhatXuatXu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCapNhatXuatXu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatXuatXuActionPerformed(evt);
            }
        });
        jPanel28.add(btnCapNhatXuatXu);

        btnXoaXuatXu.setBackground(new java.awt.Color(255, 153, 153));
        btnXoaXuatXu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Delete.png"))); // NOI18N
        btnXoaXuatXu.setText("Xoá");
        btnXoaXuatXu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoaXuatXu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaXuatXuActionPerformed(evt);
            }
        });
        jPanel28.add(btnXoaXuatXu);

        btnMoiXuatXu.setBackground(new java.awt.Color(204, 204, 204));
        btnMoiXuatXu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Document.png"))); // NOI18N
        btnMoiXuatXu.setText("Mới");
        btnMoiXuatXu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMoiXuatXu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiXuatXuActionPerformed(evt);
            }
        });
        jPanel28.add(btnMoiXuatXu);

        tblXuatXu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Mã quốc gia", "Tên quốc gia"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblXuatXu.setRowHeight(25);
        tblXuatXu.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblXuatXu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblXuatXuMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblXuatXu);

        txtTenQG.setName("Tên quốc gia"); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTenQG, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaQG, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtMaQG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtTenQG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(107, 185, 240));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ảnh sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Mã ảnh sản phẩm:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Tên ảnh sản phẩm:");

        txtMaAnhSP.setName("Mã ảnh sản phẩm"); // NOI18N

        jPanel29.setBackground(new java.awt.Color(107, 185, 240));

        btnThemAnhSP.setBackground(new java.awt.Color(153, 255, 153));
        btnThemAnhSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnThemAnhSP.setText("Thêm");
        btnThemAnhSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemAnhSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemAnhSPActionPerformed(evt);
            }
        });
        jPanel29.add(btnThemAnhSP);

        btnCapNhatAnhSP.setBackground(new java.awt.Color(255, 255, 102));
        btnCapNhatAnhSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Edit.png"))); // NOI18N
        btnCapNhatAnhSP.setText("Cập nhật");
        btnCapNhatAnhSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCapNhatAnhSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatAnhSPActionPerformed(evt);
            }
        });
        jPanel29.add(btnCapNhatAnhSP);

        btnXoaAnhSP.setBackground(new java.awt.Color(255, 153, 153));
        btnXoaAnhSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Delete.png"))); // NOI18N
        btnXoaAnhSP.setText("Xoá");
        btnXoaAnhSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoaAnhSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaAnhSPActionPerformed(evt);
            }
        });
        jPanel29.add(btnXoaAnhSP);

        btnMoiAnhSP.setBackground(new java.awt.Color(204, 204, 204));
        btnMoiAnhSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Document.png"))); // NOI18N
        btnMoiAnhSP.setText("Mới");
        btnMoiAnhSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMoiAnhSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiAnhSPActionPerformed(evt);
            }
        });
        jPanel29.add(btnMoiAnhSP);

        btnWebcam.setBackground(new java.awt.Color(255, 102, 255));
        btnWebcam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Camera.png"))); // NOI18N
        btnWebcam.setText("Chụp ảnh");
        btnWebcam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnWebcam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWebcamActionPerformed(evt);
            }
        });
        jPanel29.add(btnWebcam);

        btnChonAnhSP.setBackground(new java.awt.Color(153, 153, 255));
        btnChonAnhSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Upload.png"))); // NOI18N
        btnChonAnhSP.setText("Chọn ảnh");
        btnChonAnhSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChonAnhSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonAnhSPActionPerformed(evt);
            }
        });
        jPanel29.add(btnChonAnhSP);

        tblAnhSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Mã ảnh sản phẩm", "Tên ảnh sản phẩm"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAnhSP.setRowHeight(25);
        tblAnhSP.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblAnhSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAnhSPMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblAnhSP);

        txtTenAnhSP.setName("Tên ảnh sản phẩm"); // NOI18N

        jPanel12.setBackground(new java.awt.Color(107, 185, 240));
        jPanel12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));

        lblAnhSP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnhSP, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnhSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTenAnhSP, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaAnhSP, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtMaAnhSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtTenAnhSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(145, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(87, Short.MAX_VALUE))
        );

        tbpSanPham.addTab("Thuộc tính sản phẩm", jPanel8);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbpSanPham)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbpSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void rdoNhomHangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoNhomHangItemStateChanged
        CardLayout cardLayout = (CardLayout) pnlNhomLoaiHang.getLayout();
        cardLayout.first(pnlNhomLoaiHang);
    }//GEN-LAST:event_rdoNhomHangItemStateChanged

    private void rdoLoaihangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoLoaihangItemStateChanged
        CardLayout cardLayout = (CardLayout) pnlNhomLoaiHang.getLayout();
        cardLayout.last(pnlNhomLoaiHang);
    }//GEN-LAST:event_rdoLoaihangItemStateChanged

    private void rdoThuongHieuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoThuongHieuItemStateChanged
        CardLayout cardLayout = (CardLayout) pnlThuongHieuDongSP.getLayout();
        cardLayout.first(pnlThuongHieuDongSP);
    }//GEN-LAST:event_rdoThuongHieuItemStateChanged

    private void rdoDongSPItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoDongSPItemStateChanged
        CardLayout cardLayout = (CardLayout) pnlThuongHieuDongSP.getLayout();
        cardLayout.last(pnlThuongHieuDongSP);
    }//GEN-LAST:event_rdoDongSPItemStateChanged

    private void btnWebcamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWebcamActionPerformed
        WebcamUtils.chupAnh("SanPham");
    }//GEN-LAST:event_btnWebcamActionPerformed

    private void btnThemNhomHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemNhomHangActionPerformed
        try {
            if (checkNull(txtMaNhomHang, txtTenNhomHang)) {
                return;
            }
            if (checkTrungMa(txtMaNhomHang, tblNhomHang)) {
                MsgBox.alert(this, "Trùng mã nhóm hàng");
                return;
            }
            NhomHang nh = getFormNhomHang();
            nhomHangDAO.insert(nh);
            fillTableNhomHang();
            rowNH = tblNhomHang.getRowCount() - 1;
            tblNhomHang.setRowSelectionInterval(rowNH, rowNH);
            updateStatus(rowNH, txtMaNhomHang, btnThemNhomHang, btnCapNhatNhomHang, btnXoaNhomHang);
            MsgBox.alert(this, "Thêm thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Thêm nhóm hàng thất bại");
        }
    }//GEN-LAST:event_btnThemNhomHangActionPerformed

    private void btnMoiNhomHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiNhomHangActionPerformed
        txtMaNhomHang.setText("");
        txtTenNhomHang.setText("");
        txtGhiChuNhomHang.setText("");
        rowNH = -1;
        updateStatus(rowNH, txtMaNhomHang, btnThemNhomHang, btnCapNhatNhomHang, btnXoaNhomHang);
    }//GEN-LAST:event_btnMoiNhomHangActionPerformed

    private void tblNhomHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhomHangMouseClicked
        try {
            rowNH = tblNhomHang.getSelectedRow();
            String maNH = tblNhomHang.getValueAt(rowNH, 0).toString();
            NhomHang nh = nhomHangDAO.selectById(maNH);
            this.setFormNhomHang(nh);
            updateStatus(rowNH, txtMaNhomHang, btnThemNhomHang, btnCapNhatNhomHang, btnXoaNhomHang);
        } catch (Exception e) {
            MsgBox.alert(this, e.getMessage());
        }
    }//GEN-LAST:event_tblNhomHangMouseClicked

    private void btnCapNhatNhomHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatNhomHangActionPerformed
        try {
            if (checkNull(txtMaNhomHang, txtTenNhomHang)) {
                return;
            }
            NhomHang nh = getFormNhomHang();
            nhomHangDAO.update(nh);
            fillTableNhomHang();
            tblNhomHang.setRowSelectionInterval(rowNH, rowNH);
            MsgBox.alert(this, "Cập nhật thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật nhóm hàng thất bại");
        }
    }//GEN-LAST:event_btnCapNhatNhomHangActionPerformed

    private void btnThemThuongHieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemThuongHieuActionPerformed
        try {
            if (checkNull(txtMaThuongHieu, txtTenThuongHieu)) {
                return;
            }
            for (ThuongHieu th : listTH) {
                if (txtMaThuongHieu.getText().equalsIgnoreCase(th.getMaTH())) {
                    MsgBox.alert(this, "Trùng mã thương hiệu");
                    return;
                }
            }
            ThuongHieu th = getFormThuongHieu();
            thuongHieuDAO.insert(th);
            fillTableThuongHieu();
            rowTH = tblThuongHieu.getRowCount() - 1;
            tblThuongHieu.setRowSelectionInterval(rowTH, rowTH);
            updateStatus(rowTH, txtMaThuongHieu, btnThemThuongHieu, btnCapNhatThuongHieu, btnXoaThuongHieu);
            MsgBox.alert(this, "Thêm thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Thêm thất bại");
        }
    }//GEN-LAST:event_btnThemThuongHieuActionPerformed

    private void tblThuongHieuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblThuongHieuMouseClicked
        try {
            rowTH = tblThuongHieu.getSelectedRow();
            String maNH = tblThuongHieu.getValueAt(rowTH, 0).toString();
            ThuongHieu th = thuongHieuDAO.selectById(maNH);
            this.setFormThuongHieu(th);
            updateStatus(rowTH, txtMaThuongHieu, btnThemThuongHieu, btnCapNhatThuongHieu, btnXoaThuongHieu);
        } catch (Exception e) {
            MsgBox.alert(this, e.getMessage());
        }
    }//GEN-LAST:event_tblThuongHieuMouseClicked

    private void btnCapNhatThuongHieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatThuongHieuActionPerformed
        try {
            if (checkNull(txtMaThuongHieu, txtTenThuongHieu)) {
                return;
            }
            ThuongHieu th = getFormThuongHieu();
            thuongHieuDAO.update(th);
            fillTableThuongHieu();
            MsgBox.alert(this, "Cập nhật thành công");
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Cập nhật thất bại");
        }
    }//GEN-LAST:event_btnCapNhatThuongHieuActionPerformed

    private void btnMoiThuongHieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiThuongHieuActionPerformed
        txtMaThuongHieu.setText("");
        txtTenThuongHieu.setText("");
        txtGhiChuThuongHieu.setText("");
        rowTH = -1;
        updateStatus(rowTH, txtMaThuongHieu, btnThemThuongHieu, btnCapNhatThuongHieu, btnXoaThuongHieu);
    }//GEN-LAST:event_btnMoiThuongHieuActionPerformed

    private void cbbNhomHangPhanLoaiItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbNhomHangPhanLoaiItemStateChanged
        fillTableLoaiHang();
    }//GEN-LAST:event_cbbNhomHangPhanLoaiItemStateChanged

    private void tblLoaiHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLoaiHangMouseClicked
        try {
            rowLH = tblLoaiHang.getSelectedRow();
            String maLH = tblLoaiHang.getValueAt(rowLH, 0).toString();
            LoaiHang lh = loaiHangDAO.selectById(maLH);
            setFormLoaiHang(lh);
            updateStatus(rowLH, txtMaLoaiHang, btnThemLoaiHang, btnCapNhatLoaiHang, btnXoaLoaiHang);
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, e.getMessage());
        }
    }//GEN-LAST:event_tblLoaiHangMouseClicked

    private void btnThemLoaiHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemLoaiHangActionPerformed
        try {
            if (checkNull(txtMaLoaiHang, txtTenLoaiHang)) {
                return;
            }
            for (LoaiHang lh : listLH) {
                if (txtMaLoaiHang.getText().equalsIgnoreCase(lh.getMaLoai())) {
                    MsgBox.alert(this, "Trùng mã loại hàng");
                    return;
                }
            }
            LoaiHang lh = getFormLoaiHang();
            loaiHangDAO.insert(lh);
            fillTableLoaiHang();
            rowLH = tblLoaiHang.getRowCount() - 1;
            tblLoaiHang.setRowSelectionInterval(rowLH, rowLH);
            updateStatus(rowLH, txtMaLoaiHang, btnThemLoaiHang, btnCapNhatLoaiHang, btnXoaLoaiHang);
            MsgBox.alert(this, "Thêm thành công");
        } catch (Exception e) {
            MsgBox.alert(this, e.getMessage());
        }
    }//GEN-LAST:event_btnThemLoaiHangActionPerformed

    private void btnCapNhatLoaiHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatLoaiHangActionPerformed
        try {
            if (checkNull(txtMaLoaiHang, txtTenLoaiHang)) {
                return;
            }
            LoaiHang lh = getFormLoaiHang();
            loaiHangDAO.update(lh);
            fillTableLoaiHang();
            MsgBox.alert(this, "Cập nhật thành công");
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Cập nhật thất bại");
        }
    }//GEN-LAST:event_btnCapNhatLoaiHangActionPerformed

    private void btnMoiLoaiHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiLoaiHangActionPerformed
        txtMaLoaiHang.setText("");
        txtTenLoaiHang.setText("");
        txtGhiChuLoaiHang.setText("");
        rowLH = -1;
        updateStatus(rowLH, txtMaLoaiHang, btnThemLoaiHang, btnCapNhatLoaiHang, btnXoaLoaiHang);
    }//GEN-LAST:event_btnMoiLoaiHangActionPerformed

    private void rdoDangHopTacItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoDangHopTacItemStateChanged
        fillTableThuongHieu();
    }//GEN-LAST:event_rdoDangHopTacItemStateChanged

    private void cbbThuongHieuPhanLoaiItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbThuongHieuPhanLoaiItemStateChanged
        fillTableDongSP();
    }//GEN-LAST:event_cbbThuongHieuPhanLoaiItemStateChanged

    private void rdoDangKinhDoanhItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoDangKinhDoanhItemStateChanged
        fillTableDongSP();
    }//GEN-LAST:event_rdoDangKinhDoanhItemStateChanged

    private void btnThemDongSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemDongSPActionPerformed
        try {
            if (checkNull(txtMaDongSP, txtTenDongSP)) {
                return;
            }
            for (DongSP dsp : listDSP) {
                if (txtMaDongSP.getText().equalsIgnoreCase(dsp.getMaDongSP())) {
                    MsgBox.alert(this, "Trùng mã dòng sản phẩm");
                    return;
                }
            }
            DongSP dsp = getFormDongSP();
            dongSPDAO.insert(dsp);
            fillTableDongSP();
            rowDSP = tblDongSP.getRowCount() - 1;
            tblDongSP.setRowSelectionInterval(rowDSP, rowDSP);
            updateStatus(rowDSP, txtMaDongSP, btnThemDongSP, btnCapNhatDongSP, btnXoaDongSP);
            cbbThuongHieuPhanLoai.setSelectedIndex(cbbThuongHieu.getSelectedIndex());
            MsgBox.alert(this, "Thêm thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Thêm thất bại");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnThemDongSPActionPerformed

    private void btnCapNhatDongSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatDongSPActionPerformed
        try {
            if (checkNull(txtMaDongSP, txtTenDongSP)) {
                return;
            }
            DongSP dsp = getFormDongSP();
            dongSPDAO.update(dsp);
            fillTableDongSP();
            MsgBox.alert(this, "Cập nhật thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thất bại");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCapNhatDongSPActionPerformed

    private void btnMoiDongSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiDongSPActionPerformed
        txtMaDongSP.setText("");
        txtTenDongSP.setText("");
        txtGhiChuDongSP.setText("");
        rowDSP = -1;
        updateStatus(rowDSP, txtMaDongSP, btnThemDongSP, btnCapNhatDongSP, btnXoaDongSP);
    }//GEN-LAST:event_btnMoiDongSPActionPerformed

    private void tblDongSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDongSPMouseClicked
        try {
            rowDSP = tblDongSP.getSelectedRow();
            DongSP dsp = dongSPDAO.selectById(tblDongSP.getValueAt(rowDSP, 0).toString());
            setFormDongSP(dsp);
            updateStatus(rowDSP, txtMaDongSP, btnThemDongSP, btnCapNhatDongSP, btnXoaDongSP);
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, e.getMessage());
        }
    }//GEN-LAST:event_tblDongSPMouseClicked

    private void txtMaDongSPFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaDongSPFocusLost
        txtTenDongSP.setText(txtMaDongSP.getText().toLowerCase());
    }//GEN-LAST:event_txtMaDongSPFocusLost

    private void btnXoaNhomHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaNhomHangActionPerformed
        try {
            if (checkNull(txtMaNhomHang)) {
                return;
            }
            if (!confirmDelete(txtMaNhomHang)) {
                return;
            }
            nhomHangDAO.delete(txtMaNhomHang.getText());
            fillTableNhomHang();
            btnMoiNhomHangActionPerformed(evt);
            MsgBox.alert(this, "Xoá thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Xoá thất bại do nhóm hàng đang được bán");
        }
    }//GEN-LAST:event_btnXoaNhomHangActionPerformed

    private void btnXoaLoaiHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaLoaiHangActionPerformed
        try {
            if (checkNull(txtMaLoaiHang)) {
                return;
            }
            if (!confirmDelete(txtMaLoaiHang)) {
                return;
            }
            loaiHangDAO.delete(txtMaLoaiHang.getText());
            fillTableLoaiHang();
            btnMoiLoaiHangActionPerformed(evt);
            MsgBox.alert(this, "Xoá thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Xoá thất bại do loại hàng đang được bán");
        }
    }//GEN-LAST:event_btnXoaLoaiHangActionPerformed

    private void btnXoaThuongHieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaThuongHieuActionPerformed
        try {
            if (checkNull(txtMaThuongHieu)) {
                return;
            }
            if (!confirmDelete(txtMaThuongHieu)) {
                return;
            }
            thuongHieuDAO.delete(txtMaThuongHieu.getText());
            fillTableThuongHieu();
            btnMoiThuongHieuActionPerformed(evt);
            MsgBox.alert(this, "Xoá thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Xoá thất bại do sản phẩm của thương hiệu đang được bán");
        }
    }//GEN-LAST:event_btnXoaThuongHieuActionPerformed

    private void btnXoaDongSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaDongSPActionPerformed
        try {
            if (checkNull(txtMaDongSP)) {
                return;
            }
            if (!confirmDelete(txtMaDongSP)) {
                return;
            }
            dongSPDAO.delete(txtMaDongSP.getText());
            fillTableDongSP();
            btnMoiDongSPActionPerformed(evt);
            MsgBox.alert(this, "Xoá thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Xoá thất bại do dòng sản phẩm đang được bán");
        }
    }//GEN-LAST:event_btnXoaDongSPActionPerformed

    private void btnXoaDVTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaDVTActionPerformed
        try {
            if (checkNull(txtMaDVT)) {
                return;
            }
            if (!confirmDelete(txtMaDVT)) {
                return;
            }
            donViTinhDAO.delete(txtMaDVT.getText());
            fillTableDVT();
            btnMoiDVTActionPerformed(evt);
            MsgBox.alert(this, "Xoá thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Xoá thất bại do đơn vị tính đang được sử dụng");
        }
    }//GEN-LAST:event_btnXoaDVTActionPerformed

    private void btnThemDVTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemDVTActionPerformed
        try {
            if (checkNull(txtMaDVT, txtTenDVT)) {
                return;
            }
            if (checkTrungMa(txtMaDVT, tblDVT)) {
                MsgBox.alert(this, "Trùng mã đơn vị tính");
                return;
            }
            donViTinhDAO.insert(getFormDVT());
            fillTableDVT();
            rowDVT = tblDVT.getRowCount() - 1;
            tblDVT.setRowSelectionInterval(rowDVT, rowDVT);
            updateStatus(rowDVT, txtMaDVT, btnThemDVT, btnCapNhatDVT, btnXoaDVT);
            MsgBox.alert(this, "Thêm thành công");
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Thêm thất bại");
        }
    }//GEN-LAST:event_btnThemDVTActionPerformed

    private void btnCapNhatDVTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatDVTActionPerformed
        try {
            if (checkNull(txtMaDVT, txtTenDVT)) {
                return;
            }
            DonViTinh dvt = getFormDVT();
            donViTinhDAO.update(dvt);
            fillTableDVT();
            MsgBox.alert(this, "Cập nhật thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thất bại");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCapNhatDVTActionPerformed

    private void btnMoiDVTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiDVTActionPerformed
        txtMaDVT.setText("");
        txtTenDVT.setText("");
        rowDVT = -1;
        updateStatus(rowDVT, txtMaDVT, btnThemDVT, btnCapNhatDVT, btnXoaDVT);
    }//GEN-LAST:event_btnMoiDVTActionPerformed

    private void tblDVTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDVTMouseClicked
        try {
            rowDVT = tblDVT.getSelectedRow();
            DonViTinh dvt = donViTinhDAO.selectById(tblDVT.getValueAt(rowDVT, 0).toString());
            setFormDVT(dvt);
            updateStatus(rowDVT, txtMaDVT, btnThemDVT, btnCapNhatDVT, btnXoaDVT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblDVTMouseClicked

    private void tblXuatXuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblXuatXuMouseClicked
        try {
            rowXX = tblXuatXu.getSelectedRow();
            XuatXu xx = xuatXuDAO.selectById(tblXuatXu.getValueAt(rowXX, 0).toString());
            setFormXuatXu(xx);
            updateStatus(rowXX, txtMaQG, btnThemXuatXu, btnCapNhatXuatXu, btnXoaXuatXu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblXuatXuMouseClicked

    private void btnThemXuatXuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemXuatXuActionPerformed
        try {
            if (checkNull(txtMaQG, txtTenQG)) {
                return;
            }
            if (checkTrungMa(txtMaQG, tblXuatXu)) {
                MsgBox.alert(this, "Trùng mã quốc gia");
                return;
            }
            xuatXuDAO.insert(getFormXuatXu());
            fillTableXuatXu();
            rowXX = tblXuatXu.getRowCount() - 1;
            tblXuatXu.setRowSelectionInterval(rowXX, rowXX);
            updateStatus(rowXX, txtMaQG, btnThemXuatXu, btnCapNhatXuatXu, btnXoaXuatXu);
            MsgBox.alert(this, "Thêm thành công");
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Thêm thất bại");
        }
    }//GEN-LAST:event_btnThemXuatXuActionPerformed

    private void btnCapNhatXuatXuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatXuatXuActionPerformed
        try {
            if (checkNull(txtMaQG, txtTenQG)) {
                return;
            }
            XuatXu xx = getFormXuatXu();
            xuatXuDAO.update(xx);
            fillTableXuatXu();
            MsgBox.alert(this, "Cập nhật thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thất bại");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCapNhatXuatXuActionPerformed

    private void btnXoaXuatXuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaXuatXuActionPerformed
        try {
            if (checkNull(txtMaQG)) {
                return;
            }
            if (!confirmDelete(txtMaQG)) {
                return;
            }
            xuatXuDAO.delete(txtMaQG.getText());
            fillTableXuatXu();
            btnMoiXuatXuActionPerformed(evt);
            MsgBox.alert(this, "Xoá thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Xoá thất bại do mã quốc gia đang được sử dụng");
        }
    }//GEN-LAST:event_btnXoaXuatXuActionPerformed

    private void btnMoiXuatXuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiXuatXuActionPerformed
        txtMaQG.setText("");
        txtTenQG.setText("");
        rowXX = -1;
        updateStatus(rowXX, txtMaQG, btnThemXuatXu, btnCapNhatXuatXu, btnXoaXuatXu);
    }//GEN-LAST:event_btnMoiXuatXuActionPerformed

    private void tblKhoiLuongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKhoiLuongMouseClicked
        try {
            rowKL = tblKhoiLuong.getSelectedRow();
            KhoiLuong kl = khoiLuongDAO.selectById(tblKhoiLuong.getValueAt(rowKL, 0).toString());
            setFormKhoiLuong(kl);
            updateStatus(rowKL, txtMaKhoiLuong, btnThemKhoiLuong, btnCapNhatKhoiLuong, btnXoaKhoiLuong);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblKhoiLuongMouseClicked

    private void btnThemKhoiLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemKhoiLuongActionPerformed
        try {
            if (checkNull(txtMaKhoiLuong, txtGiaTri)) {
                return;
            }
            if (checkTrungMa(txtMaKhoiLuong, tblKhoiLuong)) {
                MsgBox.alert(this, "Trùng mã khối lượng");
                return;
            }
            khoiLuongDAO.insert(getFormKhoiLuong());
            fillTableKhoiLuong();
            rowKL = tblKhoiLuong.getRowCount() - 1;
            tblKhoiLuong.setRowSelectionInterval(rowKL, rowKL);
            updateStatus(rowKL, txtMaKhoiLuong, btnThemKhoiLuong, btnXoaKhoiLuong);
            MsgBox.alert(this, "Thêm thành công");
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Thêm thất bại");
        }
    }//GEN-LAST:event_btnThemKhoiLuongActionPerformed

    private void btnCapNhatKhoiLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatKhoiLuongActionPerformed
        try {
            if (checkNull(txtMaKhoiLuong)) {
                return;
            }
            KhoiLuong kl = getFormKhoiLuong();
            khoiLuongDAO.update(kl);
            fillTableKhoiLuong();
            MsgBox.alert(this, "Cập nhật thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thất bại");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCapNhatKhoiLuongActionPerformed

    private void btnXoaKhoiLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaKhoiLuongActionPerformed
        try {
            if (checkNull(txtMaKhoiLuong)) {
                return;
            }
            if (!confirmDelete(txtMaKhoiLuong)) {
                return;
            }
            khoiLuongDAO.delete(txtMaKhoiLuong.getText());
            fillTableKhoiLuong();
            btnMoiKhoiLuongActionPerformed(evt);
            MsgBox.alert(this, "Xoá thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Xoá thất bại do khối lượng đang được sử dụng");
        }
    }//GEN-LAST:event_btnXoaKhoiLuongActionPerformed

    private void btnMoiKhoiLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiKhoiLuongActionPerformed
        txtMaKhoiLuong.setText("");
        txtGiaTri.setText("");
        rowKL = -1;
        updateStatus(rowKL, txtMaKhoiLuong, btnThemKhoiLuong, btnCapNhatKhoiLuong, btnXoaKhoiLuong);
    }//GEN-LAST:event_btnMoiKhoiLuongActionPerformed

    private void rdoDangKDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoDangKDItemStateChanged
        CardLayout cardLayout = (CardLayout) pnlTblSanPham.getLayout();
        cardLayout.first(pnlTblSanPham);
    }//GEN-LAST:event_rdoDangKDItemStateChanged

    private void rdoNgungKDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoNgungKDItemStateChanged
        CardLayout cardLayout = (CardLayout) pnlTblSanPham.getLayout();
        cardLayout.last(pnlTblSanPham);
    }//GEN-LAST:event_rdoNgungKDItemStateChanged

    private void tblAnhSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAnhSPMouseClicked
        try {
            rowASP = tblAnhSP.getSelectedRow();
            AnhSP asp = anhSpDAO.selectById(tblAnhSP.getValueAt(rowASP, 0).toString());
            setFormAnhSP(asp);
            updateStatus(rowASP, txtMaAnhSP, btnThemAnhSP, btnCapNhatAnhSP, btnXoaAnhSP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblAnhSPMouseClicked

    private void btnThemAnhSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemAnhSPActionPerformed
        try {
            if (checkNull(txtMaAnhSP, txtTenAnhSP)) {
                return;
            }
            if (lblAnhSP.getIcon() == null) {
                MsgBox.alert(this, "Chưa chọn ảnh");
                return;
            }
            if (checkTrungMa(txtMaAnhSP, tblAnhSP)) {
                MsgBox.alert(this, "Trùng mã ảnh sản phẩm");
                return;
            }
            anhSpDAO.insert(getFormAnhSP());
            fillTableAnhSP();
            rowASP = tblAnhSP.getRowCount() - 1;
            tblAnhSP.setRowSelectionInterval(rowASP, rowASP);
            updateStatus(rowASP, txtMaAnhSP, btnThemAnhSP, btnCapNhatAnhSP, btnXoaAnhSP);
            MsgBox.alert(this, "Thêm thành công");
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Thêm thất bại");
        }
    }//GEN-LAST:event_btnThemAnhSPActionPerformed

    private void btnCapNhatAnhSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatAnhSPActionPerformed
        try {
            if (checkNull(txtMaAnhSP)) {
                return;
            }
            AnhSP asp = getFormAnhSP();
            anhSpDAO.update(asp);
            fillTableAnhSP();
            MsgBox.alert(this, "Cập nhật thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thất bại");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCapNhatAnhSPActionPerformed

    private void btnXoaAnhSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaAnhSPActionPerformed
        try {
            if (checkNull(txtMaAnhSP)) {
                return;
            }
            if (!confirmDelete(txtMaAnhSP)) {
                return;
            }
            anhSpDAO.delete(txtMaAnhSP.getText());
            fillTableAnhSP();
            btnMoiAnhSPActionPerformed(evt);
            MsgBox.alert(this, "Xoá thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Xoá thất bại do ảnh sản phẩm đang được sử dụng");
        }
    }//GEN-LAST:event_btnXoaAnhSPActionPerformed

    private void btnMoiAnhSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiAnhSPActionPerformed
        txtMaAnhSP.setText("");
        txtTenAnhSP.setText("");
        lblAnhSP.setIcon(null);
        lblAnhSP.setToolTipText("");
        rowASP = -1;
        updateStatus(rowASP, txtMaAnhSP, btnThemAnhSP, btnCapNhatAnhSP, btnXoaAnhSP);
    }//GEN-LAST:event_btnMoiAnhSPActionPerformed

    private void btnChonAnhSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonAnhSPActionPerformed
        try {
            ImageUtils.openAndInsertHinhAnh("SanPham", lblAnhSP);
            txtTenAnhSP.setText(lblAnhSP.getToolTipText());
        } catch (Exception e) {
            MsgBox.alert(this, e.getMessage());
        }
    }//GEN-LAST:event_btnChonAnhSPActionPerformed

    private void tblSanPhamDangKDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamDangKDMouseClicked
        btnChonAnhSP_SP.setEnabled(true);
        btnMoiSP.setEnabled(true);
        try {
            rowSPDangKD = tblSanPhamDangKD.getSelectedRow();
            Object[] obj = sanPhamDAO.getAllAboutSanPhamDangKDByID(tblSanPhamDangKD.getValueAt(rowSPDangKD, 0).toString());
            setFormSanPham(obj);
            updateStatus(rowSPDangKD, txtMaSP, btnThemSP, btnCapNhatSP, btnUpdateStatusSP);
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, e.getMessage());
        }
    }//GEN-LAST:event_tblSanPhamDangKDMouseClicked

    private void btnThemSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSPActionPerformed
        try {
            StringBuilder sb = new StringBuilder();
            if (checkNull(txtMaSP, txtTenSP, txtDonGia, txtBarcode)) {
                return;
            }
            Validator.isNull(txtHanSD, "Hạn sử dụng chưa nhập", sb);
            if (sb.length() > 0) {
                MsgBox.alert(this, sb.toString());
                return;
            }
            if (lblAnhSP_SP.getIcon() == null) {
                MsgBox.alert(this, "Chưa chọn ảnh");
                return;
            }
            if (checkTrungMa(txtMaSP, tblSanPhamDangKD)) {
                MsgBox.alert(this, "Trùng mã sản phẩm");
                return;
            }
            sanPhamDAO.insertSanPham(getFormSanPham());
            fillTableSanPhamDangKD();
            rowSPDangKD = tblSanPhamDangKD.getRowCount() - 1;
            tblSanPhamDangKD.setRowSelectionInterval(rowSPDangKD, rowSPDangKD);
            updateStatus(rowSPDangKD, txtMaSP, btnThemSP, btnCapNhatSP, btnUpdateStatusSP);
            MsgBox.alert(this, "Thêm thành công");
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, e.getMessage());
        }
    }//GEN-LAST:event_btnThemSPActionPerformed

    private void btnCapNhatSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatSPActionPerformed
        try {
            StringBuilder sb = new StringBuilder();
            if (checkNull(txtMaSP, txtTenSP, txtDonGia, txtBarcode)) {
                return;
            }
            Validator.isNull(txtHanSD, "Hạn sử dụng chưa nhập", sb);
            if (sb.length() > 0) {
                MsgBox.alert(this, sb.toString());
                return;
            }
            if (lblAnhSP_SP.getIcon() == null) {
                MsgBox.alert(this, "Chưa chọn ảnh");
                return;
            }
            sanPhamDAO.updateSanPham(getFormSanPham());
            fillTableSanPhamDangKD();
            tblSanPhamDangKD.setRowSelectionInterval(rowSPDangKD, rowSPDangKD);
            MsgBox.alert(this, "Cập nhật thành công");
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Cập nhật thất bại");
        }
    }//GEN-LAST:event_btnCapNhatSPActionPerformed

    private void btnChonAnhSP_SPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonAnhSP_SPActionPerformed
        try {
            ImageUtils.openAndInsertHinhAnh("SanPham", lblAnhSP_SP);
        } catch (Exception e) {
            MsgBox.alert(this, e.getMessage());
        }
    }//GEN-LAST:event_btnChonAnhSP_SPActionPerformed

    private void btnMoiSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiSPActionPerformed
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtNgayXK.setDate(new Date());
        txtHanSD.setDate(null);
        spnSoLuong.setValue(0);
        txtDonGia.setText("");
        txtBarcode.setText("");
        txtGhiChuSP.setText("");
        rowSPDangKD = -1;
        lblAnhSP_SP.setIcon(null);
        lblAnhSP_SP.setToolTipText("");
        updateStatus(rowSPDangKD, txtMaSP, btnThemSP, btnCapNhatSP, btnUpdateStatusSP);
    }//GEN-LAST:event_btnMoiSPActionPerformed

    private void tblSanPhamNgungKDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamNgungKDMouseClicked
        try {
            rowSPNgungKD = tblSanPhamNgungKD.getSelectedRow();
            Object[] obj = sanPhamDAO.getAllAboutSanPhamNgungKDByID(tblSanPhamNgungKD.getValueAt(rowSPNgungKD, 0).toString());
            setFormSanPham(obj);
            updateStatus(rowSPNgungKD, txtMaSP, btnThemSP, btnCapNhatSP, btnUpdateStatusSP);
            btnThemSP.setEnabled(false);
            btnCapNhatSP.setEnabled(false);
            btnChonAnhSP_SP.setEnabled(false);
            btnMoiSP.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, e.getMessage());
        }
    }//GEN-LAST:event_tblSanPhamNgungKDMouseClicked

    private void btnQuetBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuetBarcodeActionPerformed
        new QuetBarCodeInputJFrame().setVisible(true);
    }//GEN-LAST:event_btnQuetBarcodeActionPerformed

    private void btnUpdateStatusSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateStatusSPActionPerformed
        try {
            if (!MsgBox.confirm(this, "Bạn có muốn cập nhật trạng thái các sản phẩm được chọn không?")) {
                return;
            }
            for (int i = 0; i < tblSanPhamDangKD.getRowCount(); i++) {
                String maSP = tblSanPhamDangKD.getValueAt(i, 0).toString();
                boolean trangThai = Boolean.parseBoolean(tblSanPhamDangKD.getValueAt(i, 13).toString());
                sanPhamDAO.updateTrangThaiById(trangThai, maSP);
            }
            for (int i = 0; i < tblSanPhamNgungKD.getRowCount(); i++) {
                String maSP = tblSanPhamNgungKD.getValueAt(i, 0).toString();
                boolean trangThai = Boolean.parseBoolean(tblSanPhamNgungKD.getValueAt(i, 13).toString());
                sanPhamDAO.updateTrangThaiById(trangThai, maSP);
            }
            fillTableSanPhamDangKD();
            fillTableSanPhamNgungKD();
            MsgBox.alert(this, "Cập nhật trạng thái thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật trạng thái thất bại");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnUpdateStatusSPActionPerformed

    private void tbpSanPhamMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbpSanPhamMouseEntered
        if (getBarCode) {
            txtBarcode.setText(barcode);
            getBarCode = false;
        }
    }//GEN-LAST:event_tbpSanPhamMouseEntered

    private void btnThemLoaiSPNhanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemLoaiSPNhanhActionPerformed
        String[] args = null;
        LoaiSPJDialog.main(args);
    }//GEN-LAST:event_btnThemLoaiSPNhanhActionPerformed

    private void btnThemDongSPNhanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemDongSPNhanhActionPerformed
        String[] args = null;
        DongSPJDialog.main(args);
    }//GEN-LAST:event_btnThemDongSPNhanhActionPerformed

    private void btnThemXuatXuNhanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemXuatXuNhanhActionPerformed
        String[] args = null;
        XuatXuJDialog.main(args);
    }//GEN-LAST:event_btnThemXuatXuNhanhActionPerformed

    private void btnThemDVTNhanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemDVTNhanhActionPerformed
        String[] args = null;
        DonViTinhJDialog.main(args);
    }//GEN-LAST:event_btnThemDVTNhanhActionPerformed

    private void btnThemKhoiLuongNhanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemKhoiLuongNhanhActionPerformed
        String[] args = null;
        KhoiLuongJDialog.main(args);
    }//GEN-LAST:event_btnThemKhoiLuongNhanhActionPerformed

    private void btnThemAnhSPNhanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemAnhSPNhanhActionPerformed
        String[] args = null;
        AnhSPJDialog.main(args);
    }//GEN-LAST:event_btnThemAnhSPNhanhActionPerformed

    private void btnRefreshLoaiSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshLoaiSPActionPerformed
        loadComboBoxLoaiSP();
    }//GEN-LAST:event_btnRefreshLoaiSPActionPerformed

    private void btnRefreshDongSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshDongSPActionPerformed
        loadComboBoxDongSP();
    }//GEN-LAST:event_btnRefreshDongSPActionPerformed

    private void btnRefreshXuatXuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshXuatXuActionPerformed
        loadComBoBoxXuatXu();
    }//GEN-LAST:event_btnRefreshXuatXuActionPerformed

    private void btnRefreshDVTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshDVTActionPerformed
        loadComBoBoxDonViTinh();
    }//GEN-LAST:event_btnRefreshDVTActionPerformed

    private void btnRefreshKhoiLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshKhoiLuongActionPerformed
        loadComBoBoxKhoiLuong();
    }//GEN-LAST:event_btnRefreshKhoiLuongActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhatAnhSP;
    private javax.swing.JButton btnCapNhatDVT;
    private javax.swing.JButton btnCapNhatDongSP;
    private javax.swing.JButton btnCapNhatKhoiLuong;
    private javax.swing.JButton btnCapNhatLoaiHang;
    private javax.swing.JButton btnCapNhatNhomHang;
    private javax.swing.JButton btnCapNhatSP;
    private javax.swing.JButton btnCapNhatThuongHieu;
    private javax.swing.JButton btnCapNhatXuatXu;
    private javax.swing.JButton btnChonAnhSP;
    private javax.swing.JButton btnChonAnhSP_SP;
    private javax.swing.ButtonGroup btnGrpNhomLoaiHang;
    private javax.swing.ButtonGroup btnGrpPhanLoaiDongSP;
    private javax.swing.ButtonGroup btnGrpPhanLoaiThuongHieu;
    private javax.swing.ButtonGroup btnGrpSanPham;
    private javax.swing.ButtonGroup btnGrpThuongHieuDongSP;
    private javax.swing.JButton btnMoiAnhSP;
    private javax.swing.JButton btnMoiDVT;
    private javax.swing.JButton btnMoiDongSP;
    private javax.swing.JButton btnMoiKhoiLuong;
    private javax.swing.JButton btnMoiLoaiHang;
    private javax.swing.JButton btnMoiNhomHang;
    private javax.swing.JButton btnMoiSP;
    private javax.swing.JButton btnMoiThuongHieu;
    private javax.swing.JButton btnMoiXuatXu;
    private javax.swing.JButton btnQuetBarcode;
    private javax.swing.JButton btnRefreshDVT;
    private javax.swing.JButton btnRefreshDongSP;
    private javax.swing.JButton btnRefreshKhoiLuong;
    private javax.swing.JButton btnRefreshLoaiSP;
    private javax.swing.JButton btnRefreshXuatXu;
    private javax.swing.JButton btnThemAnhSP;
    private javax.swing.JButton btnThemAnhSPNhanh;
    private javax.swing.JButton btnThemDVT;
    private javax.swing.JButton btnThemDVTNhanh;
    private javax.swing.JButton btnThemDongSP;
    private javax.swing.JButton btnThemDongSPNhanh;
    private javax.swing.JButton btnThemKhoiLuong;
    private javax.swing.JButton btnThemKhoiLuongNhanh;
    private javax.swing.JButton btnThemLoaiHang;
    private javax.swing.JButton btnThemLoaiSPNhanh;
    private javax.swing.JButton btnThemNhomHang;
    private javax.swing.JButton btnThemSP;
    private javax.swing.JButton btnThemThuongHieu;
    private javax.swing.JButton btnThemXuatXu;
    private javax.swing.JButton btnThemXuatXuNhanh;
    private javax.swing.JButton btnUpdateStatusSP;
    private javax.swing.JButton btnWebcam;
    private javax.swing.JButton btnXoaAnhSP;
    private javax.swing.JButton btnXoaDVT;
    private javax.swing.JButton btnXoaDongSP;
    private javax.swing.JButton btnXoaKhoiLuong;
    private javax.swing.JButton btnXoaLoaiHang;
    private javax.swing.JButton btnXoaNhomHang;
    private javax.swing.JButton btnXoaThuongHieu;
    private javax.swing.JButton btnXoaXuatXu;
    private javax.swing.JComboBox<String> cbbDonViTinh;
    private javax.swing.JComboBox<String> cbbDongSP;
    private javax.swing.JComboBox<String> cbbKhoiLuong;
    private javax.swing.JComboBox<String> cbbLoaiSP;
    private javax.swing.JComboBox<String> cbbNhomHang;
    private javax.swing.JComboBox<String> cbbNhomHangPhanLoai;
    private javax.swing.JComboBox<String> cbbThuongHieu;
    private javax.swing.JComboBox<String> cbbThuongHieuPhanLoai;
    private javax.swing.JComboBox<String> cbbXuatXu;
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
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblAnhSP;
    private javax.swing.JLabel lblAnhSP_SP;
    private javax.swing.JPanel pnlDongSP;
    private javax.swing.JPanel pnlLoaiHang;
    private javax.swing.JPanel pnlNhomHang;
    private javax.swing.JPanel pnlNhomLoaiHang;
    private javax.swing.JPanel pnlTblSanPham;
    private javax.swing.JPanel pnlThuongHieu;
    private javax.swing.JPanel pnlThuongHieuDongSP;
    private javax.swing.JRadioButton rdoDangHopTac;
    private javax.swing.JRadioButton rdoDangKD;
    private javax.swing.JRadioButton rdoDangKinhDoanh;
    private javax.swing.JRadioButton rdoDongSP;
    private javax.swing.JRadioButton rdoLoaihang;
    private javax.swing.JRadioButton rdoNgungHopTac;
    private javax.swing.JRadioButton rdoNgungKD;
    private javax.swing.JRadioButton rdoNgungKinhDoanh;
    private javax.swing.JRadioButton rdoNhomHang;
    private javax.swing.JRadioButton rdoThuongHieu;
    private javax.swing.JSpinner spnSoLuong;
    private javax.swing.JScrollPane srpSanPhamDangKD;
    private javax.swing.JScrollPane srpSanPhamNgungKD;
    private javax.swing.JTable tblAnhSP;
    private javax.swing.JTable tblDVT;
    private javax.swing.JTable tblDongSP;
    private javax.swing.JTable tblKhoiLuong;
    private javax.swing.JTable tblLoaiHang;
    private javax.swing.JTable tblNhomHang;
    private javax.swing.JTable tblSanPhamDangKD;
    private javax.swing.JTable tblSanPhamNgungKD;
    private javax.swing.JTable tblThuongHieu;
    private javax.swing.JTable tblXuatXu;
    private javax.swing.JTabbedPane tbpSanPham;
    private javax.swing.JTextField txtBarcode;
    private javax.swing.JTextField txtDonGia;
    private javax.swing.JTextArea txtGhiChuDongSP;
    private javax.swing.JTextArea txtGhiChuLoaiHang;
    private javax.swing.JTextArea txtGhiChuNhomHang;
    private javax.swing.JTextArea txtGhiChuSP;
    private javax.swing.JTextArea txtGhiChuThuongHieu;
    private javax.swing.JTextField txtGiaTri;
    private com.toedter.calendar.JDateChooser txtHanSD;
    private javax.swing.JTextField txtMaAnhSP;
    private javax.swing.JTextField txtMaDVT;
    private javax.swing.JTextField txtMaDongSP;
    private javax.swing.JTextField txtMaKhoiLuong;
    private javax.swing.JTextField txtMaLoaiHang;
    private javax.swing.JTextField txtMaNhomHang;
    private javax.swing.JTextField txtMaQG;
    private javax.swing.JTextField txtMaSP;
    private javax.swing.JTextField txtMaThuongHieu;
    private com.toedter.calendar.JDateChooser txtNgayXK;
    private javax.swing.JTextField txtTenAnhSP;
    private javax.swing.JTextField txtTenDVT;
    private javax.swing.JTextField txtTenDongSP;
    private javax.swing.JTextField txtTenLoaiHang;
    private javax.swing.JTextField txtTenNhomHang;
    private javax.swing.JTextField txtTenQG;
    private javax.swing.JTextField txtTenSP;
    private javax.swing.JTextField txtTenThuongHieu;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtTimKiemDongSP;
    private javax.swing.JTextField txtTimKiemThuongHieu;
    // End of variables declaration//GEN-END:variables
}
