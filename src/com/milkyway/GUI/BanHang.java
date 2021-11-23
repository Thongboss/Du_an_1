/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.GUI;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.milkyway.DAO.BanHangDAO;
import com.milkyway.DAO.HinhThucThanhToanDAO;
import com.milkyway.Model.HinhThucThanhToan;
import com.milkyway.Model.HoaDon;
import com.milkyway.Utils.Auth;
import com.milkyway.Utils.JThread;
import com.milkyway.Utils.MsgBox;
import com.milkyway.Utils.XFormater;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PC
 */
public class BanHang extends javax.swing.JPanel implements Runnable, ThreadFactory{

    /**
     * Creates new form BanHang
     */
    BanHangDAO bh = new BanHangDAO();
    String loaiHD[] = {"Chờ", "Đã thanh toán"};
    int maxindex;
    List<Object[]> listDS = bh.loadSanPham();
    List<Object[]> listSP1 = new ArrayList<>();
    List<Object[]> listGH = new ArrayList<>();
    Object[] gh, sp1, sp2, sp3, sp4, gh1, gh2, gh3, gh4;
    int index = 0, soluong = 0;
    HinhThucThanhToanDAO hinhThucThanhToanDAO = new HinhThucThanhToanDAO();

    private WebcamPanel panel = null;
    private Webcam webcam = null;

    private static final long serialVersionUID = 6441489157408381878L;
    private Executor executor = Executors.newSingleThreadExecutor(this);
    
    public BanHang() {
        initComponents();
        init();
        for (int i = 0; i < loaiHD.length; i++) {
            cbbLoaiHD.addItem(loaiHD[i]);
        }
        loadSP();
    }

    private void init() {
        lblMaNV.setText(Auth.user.getMaNV());
        lblMaNVDH.setText(Auth.user.getMaNV());
        JThread.runDateTime(lblNgaytao);
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

    void getForm() {
        HoaDon hd = new HoaDon();
    }

    List<Object[]> loadTrang(int index) {
        List<Object[]> list = bh.loadSanPham();
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

    void loadGH() {
        DefaultTableModel model = (DefaultTableModel) tblGiohang.getModel();
        model.setRowCount(0);
        for (Object[] ak : listGH) {
            double thanhtien = Integer.parseInt(ak[4].toString()) * Double.parseDouble(ak[5].toString());
            model.addRow(new Object[]{
                ak[0], ak[2], ak[4], ak[5], thanhtien
            });
        }
    }

    private void loadSP() {
        List<Object[]> list = loadTrang(index);
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Danh sách sản phẩm rỗng");
        } else if (list.size() == 1) {
            Object[] th = list.get(0);
            String anh1 = "img\\SanPham/" + th[6].toString();
            ImageIcon icon1 = new ImageIcon(new ImageIcon(anh1).getImage().getScaledInstance(170, 185, Image.SCALE_DEFAULT));
            lblAnh1.setIcon(icon1);
            lblDongsp1.setText(th[1].toString());
            lblTensp1.setText(th[2].toString() + " " + th[3].toString());
            lblKL1.setText(th[4].toString());
            lblDongia.setText(XFormater.toCurrency(Double.parseDouble(th[5].toString())));
        } else if (list.size() == 2) {
            Object[] th = list.get(0);
            String anh1 = "img\\SanPham/" + th[6].toString();
            ImageIcon icon1 = new ImageIcon(new ImageIcon(anh1).getImage().getScaledInstance(170, 185, Image.SCALE_DEFAULT));
            lblAnh1.setIcon(icon1);
            lblDongsp1.setText(th[1].toString());
            lblTensp1.setText(th[2].toString() + " " + th[3].toString());
            lblKL1.setText("Số lượng tồn: " + th[4].toString());
            lblDongia.setText("Giá: " + th[5].toString());
            Object[] tb = list.get(1);
            String anh2 = "img\\SanPham/" + tb[6].toString();
            ImageIcon icon2 = new ImageIcon(new ImageIcon(anh2).getImage().getScaledInstance(170, 185, Image.SCALE_DEFAULT));
            lblAnh2.setIcon(icon2);
            lblDongsp2.setText(tb[1].toString());
            lblTensp2.setText(tb[2].toString() + " " + tb[3].toString());
            lblKL2.setText(tb[4].toString());
            lblDongia1.setText(XFormater.toCurrency(Double.parseDouble(th[5].toString())));
        } else if (list.size() == 3) {
            Object[] th = list.get(0);
            String anh1 = "img\\SanPham/" + th[6].toString();
            ImageIcon icon1 = new ImageIcon(new ImageIcon(anh1).getImage().getScaledInstance(170, 185, Image.SCALE_DEFAULT));
            lblAnh1.setIcon(icon1);
            lblDongsp1.setText(th[1].toString());
            lblTensp1.setText(th[2].toString() + " " + th[3].toString());
            lblKL1.setText(th[4].toString());
            lblDongia.setText(XFormater.toCurrency(Double.parseDouble(th[5].toString())));
            Object[] tb = list.get(1);
            String anh2 = "img\\SanPham/" + tb[6].toString();
            ImageIcon icon2 = new ImageIcon(new ImageIcon(anh2).getImage().getScaledInstance(170, 185, Image.SCALE_DEFAULT));
            lblAnh2.setIcon(icon2);
            lblDongsp2.setText(tb[1].toString());
            lblTensp2.setText(tb[2].toString() + " " + tb[3].toString());
            lblKL2.setText(tb[4].toString());
            lblDongia1.setText(XFormater.toCurrency(Double.parseDouble(th[5].toString())));
            Object[] tn = list.get(2);
            String anh3 = "img\\SanPham/" + tn[6].toString();
            ImageIcon icon3 = new ImageIcon(new ImageIcon(anh3).getImage().getScaledInstance(170, 185, Image.SCALE_DEFAULT));
            lblAnh3.setIcon(icon3);
            lblDongsp3.setText(tn[1].toString());
            lblTensp3.setText(tn[2].toString() + " " + tn[3].toString());
            lblKL3.setText(tn[4].toString());
            lblDongia2.setText(XFormater.toCurrency(Double.parseDouble(th[5].toString())));
        } else {
            Object[] th = list.get(0);
            String anh1 = "img\\SanPham/" + th[6].toString();
            ImageIcon icon1 = new ImageIcon(new ImageIcon(anh1).getImage().getScaledInstance(170, 185, Image.SCALE_DEFAULT));
            lblAnh1.setIcon(icon1);
            lblDongsp1.setText(th[1].toString());
            lblTensp1.setText(th[2].toString() + " " + th[3].toString());
            lblKL1.setText(th[4].toString());
            lblDongia.setText(XFormater.toCurrency(Double.parseDouble(th[5].toString())));
            Object[] tb = list.get(1);
            String anh2 = "img\\SanPham/" + tb[6].toString();
            ImageIcon icon2 = new ImageIcon(new ImageIcon(anh2).getImage().getScaledInstance(170, 185, Image.SCALE_DEFAULT));
            lblAnh2.setIcon(icon2);
            lblDongsp2.setText(tb[1].toString());
            lblTensp2.setText(tb[2].toString() + " " + tb[3].toString());
            lblKL2.setText(tb[4].toString());
            lblDongia1.setText(XFormater.toCurrency(Double.parseDouble(th[5].toString())));
            Object[] tn = list.get(2);
            String anh3 = "img\\SanPham/" + tn[6].toString();
            ImageIcon icon3 = new ImageIcon(new ImageIcon(anh3).getImage().getScaledInstance(170, 185, Image.SCALE_DEFAULT));
            lblAnh3.setIcon(icon3);
            lblDongsp3.setText(tn[1].toString());
            lblTensp3.setText(tn[2].toString() + " " + tn[3].toString());
            lblKL3.setText(tn[4].toString());
            lblDongia2.setText(XFormater.toCurrency(Double.parseDouble(th[5].toString())));
            Object[] tt = list.get(3);
            String anh4 = "img\\SanPham/" + tt[6].toString();
            ImageIcon icon4 = new ImageIcon(new ImageIcon(anh4).getImage().getScaledInstance(170, 185, Image.SCALE_DEFAULT));
            lblAnh4.setIcon(icon4);
            lblDongsp4.setText(tt[1].toString());
            lblTensp4.setText(tt[2].toString() + " " + th[3].toString());
            lblKL4.setText(tt[4].toString());
            lblDongia3.setText(XFormater.toCurrency(Double.parseDouble(th[5].toString())));
        }
    }

    void fillTableHD() {
        int so = 0;
        String cbb = cbbLoaiHD.getSelectedItem().toString();
        if (cbb.equals("Chờ")) {
            so = 0;
        } else {
            so = 1;
        }
        DefaultTableModel model = (DefaultTableModel) tblDSHD.getModel();
        model.setRowCount(0);
        List<Object[]> list = bh.loadHoaDon(so);
        for (Object[] hd : list) {
            model.addRow(hd);
        }
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = null;
            BufferedImage image = null;

            if (webcam.isOpen()) {
                if ((image = webcam.getImage()) == null) {
                    continue;
                }
            }

            try {
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                result = new MultiFormatReader().decode(bitmap);
            } catch (Exception e) {
                //No result...
            }
        } while (true);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "My Thread");
        t.setDaemon(true);
        return t;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jPanel13 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblMaNV = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtTheTV = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        lblNgaytao = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblDiachi = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lblTongtien = new javax.swing.JLabel();
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
        lblTienCanTra1 = new javax.swing.JLabel();
        btnThanhToan = new javax.swing.JButton();
        btnQuet = new javax.swing.JButton();
        btnTaoHD = new javax.swing.JButton();
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
        cbbHinhthucThanhToanDatHang = new javax.swing.JComboBox<>();
        jLabel36 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtGhichuDH = new javax.swing.JTextArea();
        btnAdddonDH = new javax.swing.JButton();
        btnNewdonDH = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGiohang = new javax.swing.JTable();
        btnBo = new javax.swing.JButton();
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pnlSP2 = new javax.swing.JPanel();
        lblAnh2 = new javax.swing.JLabel();
        lblDongsp2 = new javax.swing.JLabel();
        lblTensp2 = new javax.swing.JLabel();
        lblKL2 = new javax.swing.JLabel();
        lblDongia1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
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
        jLabel13 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
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

        jLabel3.setText("Mã nhân viên");

        lblMaNV.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblMaNV.setForeground(new java.awt.Color(0, 0, 255));

        jLabel5.setText("Mã thẻ TV (Họ tên khách hàng)");

        jLabel6.setText("Ngày tạo");

        lblNgaytao.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        lblNgaytao.setForeground(new java.awt.Color(255, 255, 255));

        jLabel8.setText("Địa chỉ cửa hàng");

        lblDiachi.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        lblDiachi.setForeground(new java.awt.Color(255, 255, 255));
        lblDiachi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDiachi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51)));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDiachi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 317, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTheTV))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(32, 32, 32)
                        .addComponent(lblMaNV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(59, 59, 59)
                        .addComponent(lblNgaytao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(txtTheTV))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(lblNgaytao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDiachi, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(107, 185, 240));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chi tiết", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel10.setText("Tổng tiền hàng");

        lblTongtien.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        lblTongtien.setForeground(new java.awt.Color(51, 0, 255));
        lblTongtien.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTongtien.setText("VND 100,000");
        lblTongtien.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51)));

        jLabel12.setText("Giảm giá");

        lblGiamgia.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        lblGiamgia.setForeground(new java.awt.Color(255, 0, 0));
        lblGiamgia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGiamgia.setText("20%");
        lblGiamgia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51)));

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

        lblTienCanTra.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblTienCanTra.setForeground(new java.awt.Color(51, 51, 255));
        lblTienCanTra.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTienCanTra.setText("VND 50,0000");
        lblTienCanTra.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51)));

        jLabel31.setText("Khách trả");

        ckbNhanHD.setText("Nhận kèm hóa đơn");

        lblTienCanTra1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblTienCanTra1.setForeground(new java.awt.Color(51, 0, 255));
        lblTienCanTra1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTienCanTra1.setText("VND 5,000");
        lblTienCanTra1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51)));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(ckbChoThanhToan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, Short.MAX_VALUE)
                        .addComponent(ckbNhanHD))
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGiamgia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtPhikhac)
                            .addComponent(lblTongtien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                        .addGap(61, 61, 61)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTienkhachtra)
                            .addComponent(lblTienCanTra1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbbHinhThucThanhToan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(53, 53, 53)
                        .addComponent(lblTienCanTra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(lblTongtien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(lblGiamgia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhikhac, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbHinhThucThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTienCanTra, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTienkhachtra, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTienCanTra1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckbChoThanhToan)
                    .addComponent(ckbNhanHD))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        btnThanhToan.setBackground(new java.awt.Color(102, 255, 102));
        btnThanhToan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Dollar.png"))); // NOI18N
        btnThanhToan.setText("Thanh toán");
        btnThanhToan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnQuet.setBackground(new java.awt.Color(255, 255, 255));
        btnQuet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/barcode.png"))); // NOI18N
        btnQuet.setText("Quét mã");
        btnQuet.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnQuet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuetActionPerformed(evt);
            }
        });

        btnTaoHD.setBackground(new java.awt.Color(255, 255, 255));
        btnTaoHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnTaoHD.setText("Tạo hóa đơn");
        btnTaoHD.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

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
                        .addComponent(btnQuet)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(btnTaoHD)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnQuet)
                    .addComponent(btnThanhToan, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                    .addComponent(btnTaoHD, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnQuet, btnTaoHD});

        jTabbedPane1.addTab("Hóa đơn", jPanel3);

        jPanel4.setBackground(new java.awt.Color(107, 185, 240));

        jPanel7.setBackground(new java.awt.Color(107, 185, 240));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin chung", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel19.setText("Mã nhân viên");

        lblMaNVDH.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 0)));

        jLabel21.setText("Mã thẻ TV (Họ tên khách hàng)");

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
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
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

        cbbHinhthucThanhToanDatHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Thanh toán khi nhận hàng" }));

        jLabel36.setText("Ghi chú");

        txtGhichuDH.setColumns(20);
        txtGhichuDH.setRows(5);
        jScrollPane6.setViewportView(txtGhichuDH);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28)
                            .addComponent(jLabel32)
                            .addComponent(jLabel35))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTienDH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblGiamgiaDH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtPhiDH, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                            .addComponent(cbbHinhthucThanhToanDatHang, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(138, 138, 138)
                                .addComponent(lblKhachcantraDH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel33))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cbbHinhthucThanhToanDatHang, lblGiamgiaDH, lblKhachcantraDH, lblTienDH, txtPhiDH});

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
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPhiDH, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblKhachcantraDH, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbHinhthucThanhToanDatHang, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnAdddonDH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Add to basket.png"))); // NOI18N
        btnAdddonDH.setText("Thêm hóa đơn");

        btnNewdonDH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Add.png"))); // NOI18N
        btnNewdonDH.setText("Tạo mới");

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNewdonDH, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNewdonDH)
                    .addComponent(btnAdddonDH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
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
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jPanel1.setBackground(new java.awt.Color(107, 185, 240));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Giỏ hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        tblGiohang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGiohang.setRowHeight(25);
        jScrollPane2.setViewportView(tblGiohang);

        btnBo.setBackground(new java.awt.Color(255, 102, 102));
        btnBo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Remove from basket.png"))); // NOI18N
        btnBo.setText("Bỏ khỏi giỏ hàng");
        btnBo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBo)
                .addContainerGap())
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
        btnTimSP.setText("Tìm SP");
        btnTimSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        pnlDSSP.setLayout(new java.awt.GridLayout(1, 4));

        pnlSP1.setBackground(new java.awt.Color(107, 185, 240));

        lblAnh1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        lblAnh1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblAnh1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnh1MouseClicked(evt);
            }
        });

        lblDongsp1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblDongsp1.setForeground(new java.awt.Color(255, 255, 255));
        lblDongsp1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblDongsp1.setText("jLabel2");

        lblTensp1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblTensp1.setForeground(new java.awt.Color(255, 255, 255));
        lblTensp1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTensp1.setText("jLabel1");

        lblKL1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblKL1.setForeground(new java.awt.Color(0, 0, 204));
        lblKL1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblKL1.setText("jLabel4");

        lblDongia.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblDongia.setForeground(new java.awt.Color(0, 0, 204));
        lblDongia.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblDongia.setText("jLabel7");

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Số lượng:");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Giá:");

        javax.swing.GroupLayout pnlSP1Layout = new javax.swing.GroupLayout(pnlSP1);
        pnlSP1.setLayout(pnlSP1Layout);
        pnlSP1Layout.setHorizontalGroup(
            pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSP1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDongsp1, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(lblTensp1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlSP1Layout.createSequentialGroup()
                        .addComponent(lblAnh1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSP1Layout.createSequentialGroup()
                        .addGroup(pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblKL1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDongia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                .addComponent(lblTensp1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKL1)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblDongia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDSSP.add(pnlSP1);

        pnlSP2.setBackground(new java.awt.Color(107, 185, 240));

        lblAnh2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        lblAnh2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblAnh2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnh2MouseClicked(evt);
            }
        });

        lblDongsp2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblDongsp2.setForeground(new java.awt.Color(255, 255, 255));
        lblDongsp2.setText("jLabel2");

        lblTensp2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblTensp2.setForeground(new java.awt.Color(255, 255, 255));
        lblTensp2.setText("jLabel1");

        lblKL2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblKL2.setForeground(new java.awt.Color(0, 0, 204));
        lblKL2.setText("jLabel4");

        lblDongia1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblDongia1.setForeground(new java.awt.Color(0, 0, 204));
        lblDongia1.setText("jLabel7");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Số lượng:");

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Giá:");

        javax.swing.GroupLayout pnlSP2Layout = new javax.swing.GroupLayout(pnlSP2);
        pnlSP2.setLayout(pnlSP2Layout);
        pnlSP2Layout.setHorizontalGroup(
            pnlSP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSP2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDongsp2, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(lblTensp2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlSP2Layout.createSequentialGroup()
                        .addComponent(lblAnh2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlSP2Layout.createSequentialGroup()
                        .addGroup(pnlSP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlSP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDongia1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblKL2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblDongia1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDSSP.add(pnlSP2);

        pnlSP3.setBackground(new java.awt.Color(107, 185, 240));

        lblAnh3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        lblAnh3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblAnh3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnh3MouseClicked(evt);
            }
        });

        lblDongsp3.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblDongsp3.setForeground(new java.awt.Color(255, 255, 255));
        lblDongsp3.setText("jLabel2");

        lblTensp3.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblTensp3.setForeground(new java.awt.Color(255, 255, 255));
        lblTensp3.setText("jLabel1");

        lblKL3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblKL3.setForeground(new java.awt.Color(0, 0, 204));
        lblKL3.setText("jLabel4");

        lblDongia2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblDongia2.setForeground(new java.awt.Color(0, 0, 204));
        lblDongia2.setText("jLabel7");

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Số lượng:");

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Giá:");

        javax.swing.GroupLayout pnlSP3Layout = new javax.swing.GroupLayout(pnlSP3);
        pnlSP3.setLayout(pnlSP3Layout);
        pnlSP3Layout.setHorizontalGroup(
            pnlSP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSP3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDongsp3, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(lblTensp3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlSP3Layout.createSequentialGroup()
                        .addComponent(lblAnh3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlSP3Layout.createSequentialGroup()
                        .addGroup(pnlSP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlSP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDongia2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblKL3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblDongia2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDSSP.add(pnlSP3);

        pnlSP4.setBackground(new java.awt.Color(107, 185, 240));

        lblAnh4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        lblAnh4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblAnh4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnh4MouseClicked(evt);
            }
        });

        lblDongsp4.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblDongsp4.setForeground(new java.awt.Color(255, 255, 255));
        lblDongsp4.setText("jLabel2");

        lblTensp4.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblTensp4.setForeground(new java.awt.Color(255, 255, 255));
        lblTensp4.setText("jLabel1");

        lblKL4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblKL4.setForeground(new java.awt.Color(0, 0, 204));
        lblKL4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblKL4.setText("jLabel4");

        lblDongia3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblDongia3.setForeground(new java.awt.Color(0, 0, 204));
        lblDongia3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblDongia3.setText("jLabel7");

        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Số lượng:");

        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Giá:");

        javax.swing.GroupLayout pnlSP4Layout = new javax.swing.GroupLayout(pnlSP4);
        pnlSP4.setLayout(pnlSP4Layout);
        pnlSP4Layout.setHorizontalGroup(
            pnlSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSP4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDongsp4, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    .addComponent(lblTensp4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlSP4Layout.createSequentialGroup()
                        .addComponent(lblAnh4, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlSP4Layout.createSequentialGroup()
                        .addGroup(pnlSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel20))
                        .addGap(17, 17, 17)
                        .addGroup(pnlSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblKL4, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(lblDongia3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)))
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
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(lblDongia3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDSSP.add(pnlSP4);

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
                        .addGap(106, 106, 106)
                        .addComponent(btnBackSP)
                        .addGap(18, 18, 18)
                        .addComponent(lblSP, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblNextSP)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlDSSP, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBackSP)
                    .addComponent(lblNextSP)
                    .addComponent(txtTimSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimSP))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnTimSP, txtTimSP});

        jPanel9.setBackground(new java.awt.Color(107, 185, 240));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách hóa đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        tblDSHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã HD", "Mã NV", "Tên KH", "Trạng thái"
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
                        .addGap(18, 18, 18)
                        .addComponent(cbbLoaiHD, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

        pnlWebcam.setBackground(new java.awt.Color(255, 255, 255));
        pnlWebcam.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        javax.swing.GroupLayout pnlWebcamLayout = new javax.swing.GroupLayout(pnlWebcam);
        pnlWebcam.setLayout(pnlWebcamLayout);
        pnlWebcamLayout.setHorizontalGroup(
            pnlWebcamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 266, Short.MAX_VALUE)
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
                        .addComponent(pnlWebcam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlWebcam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbbLoaiHDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbLoaiHDItemStateChanged
        // TODO add your handling code here:
        fillTableHD();
    }//GEN-LAST:event_cbbLoaiHDItemStateChanged

    private void lblNextSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblNextSPActionPerformed
        // TODO add your handling code here:
        index++;
        if (index >= maxindex) {
            index = 0;
        }
        loadSP();
    }//GEN-LAST:event_lblNextSPActionPerformed

    private void btnBackSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackSPActionPerformed
        // TODO add your handling code here:
        index--;
        if (index < 0) {
            index = maxindex - 1;
        }
        loadSP();
    }//GEN-LAST:event_btnBackSPActionPerformed

    private void lblAnh1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnh1MouseClicked
        soluong = Integer.parseInt(MsgBox.prompt(this, "Nhập số lượng mua: "));
        gh1[4] = soluong;
        int slt = Integer.parseInt(sp1[4].toString());
        lblKL1.setText("SL tồn: " + String.valueOf(slt - soluong));
        listGH.add(gh1);
        loadGH();
    }//GEN-LAST:event_lblAnh1MouseClicked

    private void lblAnh2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnh2MouseClicked
        soluong = Integer.parseInt(MsgBox.prompt(this, "Nhập số lượng mua: "));
        gh2[4] = soluong;
        int slt = Integer.parseInt(sp2[4].toString());
        lblKL2.setText("SL tồn: " + String.valueOf(slt - soluong));
        listGH.add(gh2);
        loadGH();
    }//GEN-LAST:event_lblAnh2MouseClicked

    private void lblAnh3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnh3MouseClicked
        soluong = Integer.parseInt(MsgBox.prompt(this, "Nhập số lượng mua: "));
        gh3[4] = soluong;
        int slt = Integer.parseInt(sp3[4].toString());
        lblKL3.setText("SL tồn: " + String.valueOf(slt - soluong));
        listGH.add(gh3);
        loadGH();
    }//GEN-LAST:event_lblAnh3MouseClicked

    private void lblAnh4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnh4MouseClicked
        soluong = Integer.parseInt(MsgBox.prompt(this, "Nhập số lượng mua: "));
        gh4[4] = soluong;
        int slt = Integer.parseInt(sp4[4].toString());
        lblKL4.setText("SL tồn: " + String.valueOf(slt - soluong));
        listGH.add(gh4);
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

    private void btnQuetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuetActionPerformed
        initWebcam();
    }//GEN-LAST:event_btnQuetActionPerformed

    private void initWebcam() throws WebcamException {
        Dimension size = WebcamResolution.QVGA.getSize();
        webcam = Webcam.getWebcams().get(0); //0 is default webcam
        webcam.setViewSize(size);
        
        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);
        panel.setFPSDisplayed(true);
        
//        JButton btn = new JButton("Stop");
//        btn.setBackground(new java.awt.Color(255, 102, 255));
//        btn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                webcam.close();
//            }
//        });

        pnlWebcam.add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 268));
//        pnlWebcam.add(btn);

        executor.execute(this);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdddonDH;
    private javax.swing.JButton btnBackSP;
    private javax.swing.JButton btnBo;
    private javax.swing.JButton btnNewdonDH;
    private javax.swing.JButton btnQuet;
    private javax.swing.JButton btnTaoHD;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnTimSP;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JComboBox<String> cbbHinhThucThanhToan;
    private javax.swing.JComboBox<String> cbbHinhthucThanhToanDatHang;
    private javax.swing.JComboBox<String> cbbLoaiHD;
    private javax.swing.JCheckBox ckbChoThanhToan;
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
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
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
    private javax.swing.JLabel lblDongia;
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
    private javax.swing.JLabel lblNgayTaoDH;
    private javax.swing.JLabel lblNgaytao;
    private javax.swing.JLabel lblSP;
    private javax.swing.JLabel lblTensp1;
    private javax.swing.JLabel lblTensp2;
    private javax.swing.JLabel lblTensp3;
    private javax.swing.JLabel lblTensp4;
    private javax.swing.JLabel lblTienCanTra;
    private javax.swing.JLabel lblTienCanTra1;
    private javax.swing.JLabel lblTienDH;
    private javax.swing.JLabel lblTongtien;
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
