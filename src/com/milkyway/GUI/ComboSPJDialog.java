/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.GUI;

import com.milkyway.DAO.ChiTietComboSPDAO;
import com.milkyway.DAO.ComboSPDAO;
import com.milkyway.DAO.SanPhamDAO;
import com.milkyway.Model.ChiTietComboSP;
import com.milkyway.Model.ComBoSP;
import com.milkyway.Utils.ImageUtils;
import com.milkyway.Utils.MsgBox;
import com.milkyway.Utils.Validator;
import com.milkyway.Utils.WebcamUtils;
import com.milkyway.Utils.XCurrency;
import com.milkyway.Utils.XDate;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author DaiAustinYersin
 */
public class ComboSPJDialog extends javax.swing.JDialog {

    /**
     * Creates new form ComboSPJDialog
     */
    TableRowSorter<TableModel> sortSP, sortCombo;
    SanPhamDAO sanPhamDAO = new SanPhamDAO();
    ComboSPDAO comboSPDAO = new ComboSPDAO();
    ChiTietComboSPDAO chiTietComboSPDAO = new ChiTietComboSPDAO();
    int rowSP, rowCombo = -1;
    List<ComBoSP> listComboSP = comboSPDAO.selectAll();
    List<String> lstMaSP = new ArrayList<>();
    Map<String, Integer> maSP_soLuong;
    int soLuongSPThemVaoCombo, soLuongCombo;
    static boolean getBarCode = false;
    static String barcode = "";

    public ComboSPJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        fillToTableSanPham();
        fillToTableComboSP();
        txtNgayTao.setDate(new Date());
        txtTimKiemSP.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String str = txtTimKiemSP.getText();
                if (str.trim().length() == 0) {
                    sortSP.setRowFilter(null);
                } else {
                    //(?i) means case insensitive search
                    sortSP.setRowFilter(RowFilter.regexFilter("(?i)" + str));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String str = txtTimKiemSP.getText();
                if (str.trim().length() == 0) {
                    sortSP.setRowFilter(null);
                } else {
                    //(?i) means case insensitive search
                    sortSP.setRowFilter(RowFilter.regexFilter("(?i)" + str));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        txtTimKiemCombo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String str = txtTimKiemCombo.getText();
                if (str.trim().length() == 0) {
                    sortCombo.setRowFilter(null);
                } else {
                    //(?i) means case insensitive search
                    sortCombo.setRowFilter(RowFilter.regexFilter("(?i)" + str));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String str = txtTimKiemCombo.getText();
                if (str.trim().length() == 0) {
                    sortCombo.setRowFilter(null);
                } else {
                    //(?i) means case insensitive search
                    sortCombo.setRowFilter(RowFilter.regexFilter("(?i)" + str));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    private void fillToTableSanPham() {
        DefaultTableModel tableModel = (DefaultTableModel) tblSanPham.getModel();
        sortSP = new TableRowSorter<>(tableModel);
        tableModel.setRowCount(0);
        tblSanPham.setRowSorter(sortSP);
        List<Object[]> lst = sanPhamDAO.getAllAboutSanPhamDangKD();
        for (Object[] obj : lst) {
            tableModel.addRow(new Object[]{
                obj[0], obj[1], obj[2], obj[3], obj[4], obj[5], Integer.parseInt(obj[6].toString()), Double.parseDouble(obj[7].toString()), obj[8], obj[9], obj[10], obj[11], false
            });
        }
    }

    private void setFormSP(Object[] obj) {
        txtMaSP.setText(obj[0].toString());
        txtTenSP.setText(obj[1].toString());
        txtLoaiHang.setText(obj[2].toString());
        txtDongSP.setText(obj[3].toString());
        txtNgayXK.setText(obj[4].toString());
        txtHanSD.setText(obj[5].toString());
        txtSoLuongTon.setText(obj[6].toString());
        txtDonGia.setText(XCurrency.toCurrency(Double.parseDouble(obj[7].toString())));
        txtXuatXu.setText(obj[8].toString());
        txtKhoiLuong.setText(obj[9].toString());
        txtDonViTinh.setText(obj[10].toString());
        txtBarcodeSP.setText(obj[11].toString());
        lblAnhSP.setIcon(ImageUtils.resizeImg(ImageUtils.read("SanPham", obj[14].toString()), lblAnhSP));
    }

    private ComBoSP getFormComboSP() {
        ComBoSP cb = new ComBoSP();
        cb.setMaComboSP(txtMaCombo.getText());
        cb.setTenComboSP(txtTenCombo.getText());
        cb.setSoLuong(Integer.parseInt(spnSoLuong.getValue().toString()));
        cb.setDonGia(Double.parseDouble(txtTongGia.getText()));
        cb.setNgayTao(txtNgayTao.getDate());
        cb.setNgayHetHan(txtNgayHetHan.getDate());
        cb.setBarcode(txtBarcodeCombo.getText());
        cb.setGhiChu(txtGhiChuCombo.getText());
        cb.setAnhComboSP(lblAnhCombo.getToolTipText());
        return cb;
    }

    private void setFormComboSP(ComBoSP cb) {
        txtMaCombo.setText(cb.getMaComboSP());
        txtTenCombo.setText(cb.getTenComboSP());
        spnSoLuong.setValue(cb.getSoLuong());
        txtTongGia.setText(cb.getDonGia() + "");
        txtNgayTao.setDate(cb.getNgayTao());
        txtNgayHetHan.setDate(cb.getNgayHetHan());
        txtBarcodeCombo.setText(cb.getBarcode());
        txtGhiChuCombo.setText(cb.getGhiChu());
        if (cb.getAnhComboSP() != null) {
            lblAnhCombo.setIcon(ImageUtils.resizeImg(ImageUtils.read("ComboSanPham", cb.getAnhComboSP()), lblAnhCombo));
            lblAnhCombo.setToolTipText(cb.getAnhComboSP());
        }
    }

    private void updateStatus(int row, JTextField txt, JButton btnThem, JButton... btn) {
        boolean edit = row >= 0;
        txt.setEditable(!edit);
        btnThem.setEnabled(!edit);
        for (int i = 0; i < btn.length; i++) {
            btn[i].setEnabled(edit);
        }
    }

    private void fillToTableComboSP() {
        DefaultTableModel tableModel = (DefaultTableModel) tblComboSP.getModel();
        sortCombo = new TableRowSorter<>(tableModel);
        tableModel.setRowCount(0);
        tblComboSP.setRowSorter(sortCombo);
        try {
            List<ComBoSP> lst;
            if (rdoConHan.isSelected()) {
                lst = comboSPDAO.selectAll_ConHan();
            } else {
                lst = comboSPDAO.selectAll_HetHan();
            }
            for (ComBoSP cb : lst) {
                tableModel.addRow(new Object[]{
                    cb.getMaComboSP(), cb.getTenComboSP(), cb.getSoLuong(), cb.getDonGia(), cb.getNgayTao(), cb.getNgayHetHan(), cb.getBarcode(), cb.getGhiChu()
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        tbpComboSP = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtMaCombo = new javax.swing.JTextField();
        spnSoLuong = new javax.swing.JSpinner();
        txtTongGia = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtNgayHetHan = new com.toedter.calendar.JDateChooser();
        txtNgayTao = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtGhiChuCombo = new javax.swing.JTextArea();
        jPanel14 = new javax.swing.JPanel();
        lblAnhCombo = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        txtTimKiemCombo = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblComboSP = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        rdoConHan = new javax.swing.JRadioButton();
        rdoHetHan = new javax.swing.JRadioButton();
        txtBarcodeCombo = new javax.swing.JTextField();
        btnThemBarcode = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtTenCombo = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        btnThem = new javax.swing.JButton();
        btnCapNhat = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        btnWebcam = new javax.swing.JButton();
        btnChonAnh = new javax.swing.JButton();
        btnConfirmSL = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        txtTimKiemSP = new javax.swing.JTextField();
        srpSanPhamDangKD = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        lblAnhSP = new javax.swing.JLabel();
        txtTenSP = new javax.swing.JTextField();
        txtMaSP = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtDonGia = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtLoaiHang = new javax.swing.JTextField();
        txtDongSP = new javax.swing.JTextField();
        txtXuatXu = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtKhoiLuong = new javax.swing.JTextField();
        txtDonViTinh = new javax.swing.JTextField();
        txtBarcodeSP = new javax.swing.JTextField();
        txtSoLuongTon = new javax.swing.JTextField();
        txtNgayXK = new javax.swing.JTextField();
        txtHanSD = new javax.swing.JTextField();
        btnThemVaoCombo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Quản lý Combo sản phẩm");

        jPanel1.setBackground(new java.awt.Color(107, 185, 240));

        tbpComboSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tbpComboSPMouseEntered(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(107, 185, 240));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Mã Combo:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Tên Combo:");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Số lượng Combo có thể mua:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Tổng giá:");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Ngày tạo:");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Ngày hết hạn:");

        spnSoLuong.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnSoLuongStateChanged(evt);
            }
        });

        txtTongGia.setName("Tổng giá"); // NOI18N

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Barcode:");

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Ghi chú:");

        txtNgayTao.setEnabled(false);

        txtGhiChuCombo.setColumns(20);
        txtGhiChuCombo.setLineWrap(true);
        txtGhiChuCombo.setRows(5);
        jScrollPane1.setViewportView(txtGhiChuCombo);

        jPanel14.setBackground(new java.awt.Color(107, 185, 240));
        jPanel14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));

        lblAnhCombo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAnhCombo.setToolTipText("");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnhCombo, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnhCombo, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(107, 185, 240));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTimKiemCombo, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(txtTimKiemCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        tblComboSP.setAutoCreateRowSorter(true);
        tblComboSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã Combo", "Tên Combo", "Số lượng", "Tổng giá", "Ngày tạo", "Ngày hết hạn", "Barcode", "Ghi chú"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblComboSP.setRowHeight(25);
        tblComboSP.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblComboSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblComboSPMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblComboSP);

        jPanel6.setBackground(new java.awt.Color(107, 185, 240));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Phân loại", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        buttonGroup1.add(rdoConHan);
        rdoConHan.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        rdoConHan.setForeground(new java.awt.Color(255, 255, 255));
        rdoConHan.setSelected(true);
        rdoConHan.setText("Còn hạn");
        rdoConHan.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        rdoConHan.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rdoConHanStateChanged(evt);
            }
        });

        buttonGroup1.add(rdoHetHan);
        rdoHetHan.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        rdoHetHan.setForeground(new java.awt.Color(255, 255, 255));
        rdoHetHan.setText("Hết hạn");
        rdoHetHan.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rdoConHan)
                .addGap(18, 18, 18)
                .addComponent(rdoHetHan)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoConHan)
                    .addComponent(rdoHetHan))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        btnThemBarcode.setBackground(new java.awt.Color(255, 255, 255));
        btnThemBarcode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Add.png"))); // NOI18N
        btnThemBarcode.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemBarcodeActionPerformed(evt);
            }
        });

        txtTenCombo.setColumns(20);
        txtTenCombo.setLineWrap(true);
        txtTenCombo.setRows(5);
        jScrollPane3.setViewportView(txtTenCombo);

        jPanel7.setBackground(new java.awt.Color(107, 185, 240));

        btnThem.setBackground(new java.awt.Color(153, 255, 153));
        btnThem.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Create.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });
        jPanel7.add(btnThem);

        btnCapNhat.setBackground(new java.awt.Color(255, 255, 102));
        btnCapNhat.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnCapNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Edit.png"))); // NOI18N
        btnCapNhat.setText("Cập nhật");
        btnCapNhat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });
        jPanel7.add(btnCapNhat);

        btnMoi.setBackground(new java.awt.Color(204, 204, 204));
        btnMoi.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Document.png"))); // NOI18N
        btnMoi.setText("Mới");
        btnMoi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });
        jPanel7.add(btnMoi);

        btnWebcam.setBackground(new java.awt.Color(255, 102, 255));
        btnWebcam.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnWebcam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Camera.png"))); // NOI18N
        btnWebcam.setText("Chụp ảnh");
        btnWebcam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnWebcam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWebcamActionPerformed(evt);
            }
        });
        jPanel7.add(btnWebcam);

        btnChonAnh.setBackground(new java.awt.Color(153, 153, 255));
        btnChonAnh.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnChonAnh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Upload.png"))); // NOI18N
        btnChonAnh.setText("Chọn ảnh");
        btnChonAnh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChonAnh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonAnhActionPerformed(evt);
            }
        });
        jPanel7.add(btnChonAnh);

        btnConfirmSL.setBackground(new java.awt.Color(255, 255, 255));
        btnConfirmSL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Tick.png"))); // NOI18N
        btnConfirmSL.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConfirmSL.setEnabled(false);
        btnConfirmSL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmSLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtMaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(spnSoLuong)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnConfirmSL))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addGap(31, 31, 31)
                                        .addComponent(txtTongGia, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(103, 103, 103)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel21))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtBarcodeCombo)
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnThemBarcode))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtNgayHetHan, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(txtMaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnConfirmSL)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel15)
                                        .addComponent(spnSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTongGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtNgayHetHan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtBarcodeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel21))
                                    .addComponent(btnThemBarcode))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel22)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(29, 29, 29)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                .addContainerGap())
        );

        tbpComboSP.addTab("Combo sản phẩm", jPanel3);

        jPanel2.setBackground(new java.awt.Color(107, 185, 240));

        jPanel4.setBackground(new java.awt.Color(107, 185, 240));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTimKiemSP, javax.swing.GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(txtTimKiemSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblSanPham.setAutoCreateRowSorter(true);
        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Loại hàng", "Dòng sản phẩm", "Ngày xuất kho", "Hạn sử dụng", "Số lượng tồn", "Đơn giá", "Xuất xứ", "Khối lượng", "Đơn vị tính", "Barcode", "Thêm vào Combo", "Số lượng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSanPham.setRowHeight(25);
        tblSanPham.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamMouseClicked(evt);
            }
        });
        srpSanPhamDangKD.setViewportView(tblSanPham);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Mã sản phẩm:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Tên sản phẩm:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Loại hàng:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Dòng sản phẩm:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Ngày xuất kho:");

        jPanel13.setBackground(new java.awt.Color(107, 185, 240));
        jPanel13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));

        lblAnhSP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAnhSP.setToolTipText("");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnhSP, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnhSP, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtTenSP.setEditable(false);

        txtMaSP.setEditable(false);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Hạn sử dụng:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Số lượng tồn:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Đơn giá:");

        txtDonGia.setEditable(false);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Xuất xứ:");

        txtLoaiHang.setEditable(false);

        txtDongSP.setEditable(false);

        txtXuatXu.setEditable(false);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Khối lượng:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Barcode:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Đơn vị tính:");

        txtKhoiLuong.setEditable(false);

        txtDonViTinh.setEditable(false);

        txtBarcodeSP.setEditable(false);

        txtSoLuongTon.setEditable(false);

        txtNgayXK.setEditable(false);

        txtHanSD.setEditable(false);

        btnThemVaoCombo.setBackground(new java.awt.Color(102, 255, 102));
        btnThemVaoCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnThemVaoCombo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Add.png"))); // NOI18N
        btnThemVaoCombo.setText("Thêm vào Combo");
        btnThemVaoCombo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemVaoCombo.setEnabled(false);
        btnThemVaoCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemVaoComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(srpSanPhamDangKD)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTenSP, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                                    .addComponent(txtMaSP)
                                    .addComponent(txtLoaiHang)
                                    .addComponent(txtDongSP)
                                    .addComponent(txtNgayXK)
                                    .addComponent(txtHanSD))
                                .addGap(143, 143, 143)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtKhoiLuong, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                                    .addComponent(txtXuatXu)
                                    .addComponent(txtDonGia)
                                    .addComponent(txtDonViTinh)
                                    .addComponent(txtBarcodeSP)
                                    .addComponent(txtSoLuongTon))
                                .addGap(117, 117, 117))
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnThemVaoCombo))
                        .addGap(42, 42, 42)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(txtLoaiHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(txtDongSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(txtNgayXK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(txtHanSD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(txtSoLuongTon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(txtDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(txtXuatXu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(txtKhoiLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(txtDonViTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(txtBarcodeSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnThemVaoCombo)
                        .addGap(7, 7, 7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(srpSanPhamDangKD, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
                .addContainerGap())
        );

        tbpComboSP.addTab("Sản phẩm", jPanel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbpComboSP)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbpComboSP)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamMouseClicked
        try {
            rowSP = tblSanPham.getSelectedRow();
            Object[] obj = sanPhamDAO.getAllAboutSanPhamDangKDByID(tblSanPham.getValueAt(rowSP, 0).toString());
            setFormSP(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblSanPhamMouseClicked

    private void btnThemBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemBarcodeActionPerformed
        new QuetBarCodeInputJFrame().setVisible(true);
    }//GEN-LAST:event_btnThemBarcodeActionPerformed

    private void btnWebcamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWebcamActionPerformed
        WebcamUtils.chupAnh("ComboSanPham");
    }//GEN-LAST:event_btnWebcamActionPerformed

    private void btnChonAnhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonAnhActionPerformed
        try {
            ImageUtils.openAndInsertHinhAnh("ComboSanPham", lblAnhCombo);
        } catch (Exception e) {
            MsgBox.alert(this, e.getMessage());
        }
    }//GEN-LAST:event_btnChonAnhActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        try {
            StringBuilder sb = new StringBuilder();
            if (txtMaCombo.getText().trim().isEmpty()) {
                Validator.isNull(txtMaCombo, "Mã Combo chưa nhập", sb);
            } else {
                for (ComBoSP cb : listComboSP) {
                    if (txtMaCombo.getText().trim().equalsIgnoreCase(cb.getMaComboSP())) {
                        sb.append("Mã Combo sản phẩm đã tồn tại").append("\n");
                        txtMaCombo.setBackground(Color.red);
                    }
                }
            }

            Validator.isNull(txtTenCombo, "Tên Combo chưa nhập", sb);
            Validator.checkSoNguyenDuong(spnSoLuong.getValue(), sb);
            Validator.checkMoney(txtTongGia, sb);
            Validator.checkHanSD(txtNgayHetHan, sb);

            if (sb.length() > 0) {
                MsgBox.alert(this, sb.toString());
                return;
            }
            txtMaCombo.setBackground(Color.white);
            comboSPDAO.insert(getFormComboSP());

            Set<String> maSP = maSP_soLuong.keySet();
            for (String key : maSP) {
                ChiTietComboSP chiTietComboSP = new ChiTietComboSP();
                chiTietComboSP.setIDComboSP(comboSPDAO.selectById(txtMaCombo.getText()).getIDComboSP());
                chiTietComboSP.setIDChiTietSP(sanPhamDAO.select_Id_ChiTietSP_By_MaSP(key));
                chiTietComboSP.setSoLuongSP(maSP_soLuong.get(key) / soLuongCombo);
                chiTietComboSPDAO.insert(chiTietComboSP);

                Object[] obj = sanPhamDAO.getAllAboutSanPhamDangKDByID(key);
                obj[6] = Integer.parseInt(obj[6].toString()) - maSP_soLuong.get(key);
                sanPhamDAO.update_SoLuong_By_MaSP(key, Integer.parseInt(obj[6].toString()));
            }
            fillToTableComboSP();
            MsgBox.alert(this, "Thêm thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Thêm thất bại");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        rowCombo = -1;
        fillToTableSanPham();
        maSP_soLuong = new HashMap<>();
        updateStatus(rowCombo, txtMaCombo, btnConfirmSL, btnThem, btnCapNhat, btnThemBarcode, btnChonAnh, btnWebcam);
        txtMaCombo.setText("");
        txtTenCombo.setText("");
        txtTongGia.setText("");
        txtNgayTao.setDate(new Date());
        txtNgayHetHan.setDate(null);
        txtBarcodeCombo.setText("");
        txtGhiChuCombo.setText("");
        spnSoLuong.setEnabled(true);
        spnSoLuong.setValue(1);
        spnSoLuong.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.yellow, java.awt.Color.yellow));
        lblAnhCombo.setIcon(null);
        MsgBox.alert(this, "Nhập số lượng Combo cần tạo");
    }//GEN-LAST:event_btnMoiActionPerformed

    private void spnSoLuongStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnSoLuongStateChanged
        if (Integer.parseInt(spnSoLuong.getValue().toString()) > 0) {
            soLuongCombo = Integer.parseInt(spnSoLuong.getValue().toString());
            btnConfirmSL.setEnabled(true);
            spnSoLuong.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.yellow, java.awt.Color.yellow));
        } else {
            spnSoLuong.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.red, java.awt.Color.red));
            btnConfirmSL.setEnabled(false);
        }
    }//GEN-LAST:event_spnSoLuongStateChanged

    private void btnConfirmSLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmSLActionPerformed
        tbpComboSP.setSelectedIndex(1);
        soLuongSPThemVaoCombo = 0;
        Object test;
        while (true) {
            try {
                test = MsgBox.prompt(this, "Nhập số lượng sản phẩm muốn thêm vào combo").trim();
                soLuongSPThemVaoCombo = Integer.parseInt(test.toString());
                btnThemVaoCombo.setEnabled(true);
                MsgBox.alert(this, "Vui lòng chọn " + soLuongSPThemVaoCombo + " sản phẩm thêm vào Combo");
                break;
            } catch (NullPointerException e) {
                MsgBox.alert(this, "Vui lòng nhập số lượng");
            } catch (NumberFormatException e) {
                MsgBox.alert(this, "Vui lòng nhập số");
            }
        }
    }//GEN-LAST:event_btnConfirmSLActionPerformed

    private void btnThemVaoComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemVaoComboActionPerformed
        try {
            double tongGia = 0;
            int soLuongSP = 0;
            maSP_soLuong.clear();
            lstMaSP.clear();
            for (int i = 0; i < tblSanPham.getRowCount(); i++) {
                Object tbl_MaSP = tblSanPham.getValueAt(i, 0);
                Object tbl_ThemVaoCombo = tblSanPham.getValueAt(i, 12);
                Object tbl_SoLuong = tblSanPham.getValueAt(i, 13);
                Object tbl_DonGia = tblSanPham.getValueAt(i, 7);
                Object tbl_SoLuongTon = tblSanPham.getValueAt(i, 6);
                if (tbl_SoLuong != null) {
                    if (Integer.parseInt(tbl_SoLuong.toString()) < 0) {
                        MsgBox.alert(this, "Số lượng phải lớn hơn không");
                        return;
                    }
                    if (Boolean.parseBoolean(tbl_ThemVaoCombo.toString()) == true && Integer.parseInt(tbl_SoLuong.toString()) > 0) {
                        maSP_soLuong.put(tbl_MaSP.toString(), Integer.parseInt(tbl_SoLuong.toString()) * soLuongCombo);
                        int j = 1;
                        while (j <= Integer.parseInt(tbl_SoLuong.toString())) {
                            lstMaSP.add(tbl_MaSP.toString());
                            j++;
                            tongGia += Double.parseDouble(tbl_DonGia.toString());
                        }
                        soLuongSP += Integer.parseInt(tbl_SoLuong.toString());
                        tbl_SoLuongTon = Integer.parseInt(tbl_SoLuongTon.toString()) - Integer.parseInt(tbl_SoLuong.toString()) * soLuongCombo;
                        if (Integer.parseInt(tbl_SoLuongTon.toString()) < 0) {
                            MsgBox.alert(this, "Không đủ hàng để tạo hóa đơn");
                            return;
                        }
                        tblSanPham.setValueAt(tbl_SoLuongTon, i, 6);
                    }
                }
            }
            if (soLuongSPThemVaoCombo != soLuongSP) {
                MsgBox.alert(this, "Vui lòng chọn sản phẩm và nhập đúng " + soLuongSPThemVaoCombo + " sản phẩm");
                return;
            }
            btnThem.setEnabled(true);
            btnThemBarcode.setEnabled(true);
            txtMaCombo.setText("Combo_");
            txtTenCombo.setText("Combo " + soLuongSPThemVaoCombo + " sản phẩm - ");
            txtNgayHetHan.setDate(XDate.toDate(sanPhamDAO.getAllAboutSanPhamDangKDByID(lstMaSP.get(0))[5].toString(), "yyyy-MM-dd"));

            Set<String> maSP = maSP_soLuong.keySet();
            for (String key : maSP) {
//                System.out.println(key + " - " + maSP_soLuong.get(key));
                Object[] obj = sanPhamDAO.getAllAboutSanPhamDangKDByID(key);
                if (!txtMaCombo.getText().contains(key)) {
                    txtMaCombo.setText(txtMaCombo.getText() + key + "_");
                    txtTenCombo.setText(txtTenCombo.getText() + obj[1] + " + ");
                }
                Date ngayHetHan = XDate.toDate(obj[5].toString(), "yyyy-MM-dd");
                if (ngayHetHan.before(txtNgayHetHan.getDate())) {
                    txtNgayHetHan.setDate(ngayHetHan);
                }
            }
            if (txtNgayHetHan.getDate().equals(new Date())) {
                MsgBox.alert(this, "Không thể tạo Combo do sản phẩm đã đến hoặc quá hạn sử dụng");
                return;
            }
            txtTongGia.setText(tongGia + "");
            btnConfirmSL.setEnabled(false);
            btnThemVaoCombo.setEnabled(false);
            spnSoLuong.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Spinner.border"));
            btnChonAnh.setEnabled(true);
            btnWebcam.setEnabled(true);
            tbpComboSP.setSelectedIndex(0);
        } catch (NumberFormatException n) {
            MsgBox.alert(this, "Số lượng phải là số tự nhiên");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnThemVaoComboActionPerformed

    private void tblComboSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblComboSPMouseClicked
        try {
            rowCombo = tblComboSP.getSelectedRow();
            ComBoSP cb = comboSPDAO.selectById(tblComboSP.getValueAt(rowCombo, 0).toString());
            setFormComboSP(cb);
            spnSoLuong.setEnabled(false);
            btnConfirmSL.setEnabled(false);
            spnSoLuong.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("Spinner.border"));
            updateStatus(rowCombo, txtMaCombo, btnThem, btnCapNhat, btnChonAnh, btnWebcam, btnThemBarcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblComboSPMouseClicked

    private void rdoConHanStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rdoConHanStateChanged
        fillToTableComboSP();
    }//GEN-LAST:event_rdoConHanStateChanged

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        try {
            StringBuilder sb = new StringBuilder();
            Validator.isNull(txtMaCombo, "Mã Combo chưa nhập", sb);
            Validator.isNull(txtTenCombo, "Tên Combo chưa nhập", sb);
            Validator.checkSoNguyenDuong(spnSoLuong.getValue(), sb);
            Validator.checkMoney(txtTongGia, sb);
            Validator.checkHanSD(txtNgayHetHan, sb);

            if (sb.length() > 0) {
                MsgBox.alert(this, sb.toString());
                return;
            }

            comboSPDAO.update(getFormComboSP());
            fillToTableComboSP();
            MsgBox.alert(this, "Cập nhật thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thất bại");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void tbpComboSPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbpComboSPMouseEntered
        if (getBarCode) {
            txtBarcodeCombo.setText(barcode);
            getBarCode = false;
        }
    }//GEN-LAST:event_tbpComboSPMouseEntered

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ComboSPJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ComboSPJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ComboSPJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ComboSPJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ComboSPJDialog dialog = new ComboSPJDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnChonAnh;
    private javax.swing.JButton btnConfirmSL;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThemBarcode;
    private javax.swing.JButton btnThemVaoCombo;
    private javax.swing.JButton btnWebcam;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblAnhCombo;
    private javax.swing.JLabel lblAnhSP;
    private javax.swing.JRadioButton rdoConHan;
    private javax.swing.JRadioButton rdoHetHan;
    private javax.swing.JSpinner spnSoLuong;
    private javax.swing.JScrollPane srpSanPhamDangKD;
    private javax.swing.JTable tblComboSP;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTabbedPane tbpComboSP;
    private javax.swing.JTextField txtBarcodeCombo;
    private javax.swing.JTextField txtBarcodeSP;
    private javax.swing.JTextField txtDonGia;
    private javax.swing.JTextField txtDonViTinh;
    private javax.swing.JTextField txtDongSP;
    private javax.swing.JTextArea txtGhiChuCombo;
    private javax.swing.JTextField txtHanSD;
    private javax.swing.JTextField txtKhoiLuong;
    private javax.swing.JTextField txtLoaiHang;
    private javax.swing.JTextField txtMaCombo;
    private javax.swing.JTextField txtMaSP;
    private com.toedter.calendar.JDateChooser txtNgayHetHan;
    private com.toedter.calendar.JDateChooser txtNgayTao;
    private javax.swing.JTextField txtNgayXK;
    private javax.swing.JTextField txtSoLuongTon;
    private javax.swing.JTextArea txtTenCombo;
    private javax.swing.JTextField txtTenSP;
    private javax.swing.JTextField txtTimKiemCombo;
    private javax.swing.JTextField txtTimKiemSP;
    private javax.swing.JTextField txtTongGia;
    private javax.swing.JTextField txtXuatXu;
    // End of variables declaration//GEN-END:variables
}
