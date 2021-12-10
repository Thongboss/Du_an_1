/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milkyway.GUI;

import com.milkyway.DAO.TheThanhVienDAO;
import com.milkyway.Model.TheThanhVien;
import com.milkyway.Utils.ImageUtils;
import com.milkyway.Utils.MsgBox;
import com.milkyway.Utils.Validator;
import com.milkyway.Utils.WebcamUtils;
import com.milkyway.Utils.XDate;
import java.awt.Color;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
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
public class TheThanhVienJPanel extends javax.swing.JPanel {

    /**
     * Creates new form NhanVienJPanel1
     */
    TheThanhVienDAO theThanhVienDAO = new TheThanhVienDAO();

    int rowConHan, rowHetHan = -1;
    JFileChooser fileChooser = new JFileChooser();
    String imageName = "";
    TableRowSorter<TableModel> sortTTV;

    public TheThanhVienJPanel() {
        initComponents();
        fillTabTheHetHan();
        fillTableTheTVConHan();
        txtNgayTao.setDate(new Date());
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String str = txtTimKiem.getText();
                if (str.trim().length() == 0) {
                    sortTTV.setRowFilter(null);
                } else {
                    //(?i) means case insensitive search
                    sortTTV.setRowFilter(RowFilter.regexFilter("(?i)" + str));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String str = txtTimKiem.getText();
                if (str.trim().length() == 0) {
                    sortTTV.setRowFilter(null);
                } else {
                    //(?i) means case insensitive search
                    sortTTV.setRowFilter(RowFilter.regexFilter("(?i)" + str));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    private void insert() {
        TheThanhVien tv = getForm();

        try {
            theThanhVienDAO.insert(tv);
            this.fillTableTheTVConHan();
            this.clearForm();
            MsgBox.alert(this, "Thêm mới thành công!");

        } catch (Exception e) {
            MsgBox.alert(this, "Thêm mới thất bại!");
            e.printStackTrace();
        }

    }

    private void clearForm() {
        TheThanhVien tv = new TheThanhVien();
        this.setForm(tv);
        this.rowConHan = -1;
        txtNgayTao.setDate(new Date());
        txtDiem.setText("0");
    }

    private TheThanhVien getForm() {
        TheThanhVien tv = new TheThanhVien();
        tv.setMaTheTV(txtMaTheTV.getText());
        tv.setTenKH(txtHoTen.getText());
        tv.setGioiTinh(rdoNam.isSelected());
        tv.setNgaySinh(txtNgaySinh.getDate());
        tv.setSDT(txtSDT.getText());
        tv.setCMND(txtCMND.getText());
        tv.setEmail(txtEmail.getText());
        tv.setNgayTao(txtNgayTao.getDate());
        tv.setNgayHetHan(txtNgayHetHan.getDate());
        tv.setHinhAnh(lblAnh.getToolTipText());
        
        return tv;

    }

    private void setForm(TheThanhVien tv) {
        txtMaTheTV.setText(tv.getMaTheTV());
        txtHoTen.setText(tv.getTenKH());
        if (tv.isGioiTinh()) {
            rdoNam.setSelected(true);
        } else {
            rdoNu.setSelected(true);

        }
        txtNgaySinh.setDate(tv.getNgaySinh());
        txtSDT.setText(tv.getSDT());
        txtCMND.setText(tv.getCMND());
        txtEmail.setText(tv.getEmail());
        txtNgayTao.setDate(tv.getNgayTao());
        txtNgayHetHan.setDate(tv.getNgayHetHan());
        txtDiem.setText(tv.getDiem() + "");
        if (tv.getHinhAnh() != null) {
            lblAnh.setIcon(ImageUtils.resizeImg(ImageUtils.read("TheThanhVien", tv.getHinhAnh()), lblAnh));
            lblAnh.setToolTipText(tv.getHinhAnh());
        }
    }

    private void edit() {
        String mattv = (String) tblTheThanhVien.getValueAt(this.rowConHan, 0);
        TheThanhVien tv = theThanhVienDAO.selectById(mattv);
        this.setForm(tv);
        this.rowConHan = -1;
    }

    private void update() {
        TheThanhVien tv = getForm();
        try {
            theThanhVienDAO.update(tv);
            this.fillTableTheTVConHan();
            MsgBox.alert(this, "Cập nhật thành công!");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thất bại!");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void fillTableTheTVConHan() {
        DefaultTableModel model = (DefaultTableModel) tblTheThanhVien.getModel();
        sortTTV = new TableRowSorter<>(model);
        model.setRowCount(0);
        tblTheThanhVien.setRowSorter(sortTTV);
        try {
            List<TheThanhVien> list = theThanhVienDAO.selectTheTVConHan();
            for (TheThanhVien ttv : list) {
                Object[] rowdata = new Object[]{
                    ttv.getMaTheTV(),
                    ttv.getTenKH(),
                    ttv.isGioiTinh() ? "Nam" : "Nữ",
                    ttv.getNgaySinh(),
                    ttv.getSDT(),
                    ttv.getCMND(),
                    ttv.getEmail(),
                    ttv.getNgayTao(),
                    ttv.getNgayHetHan(),
                    ttv.getDiem()
                };
                model.addRow(rowdata);

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void fillTabTheHetHan() {
        DefaultTableModel model = (DefaultTableModel) tblTheTVHetHan.getModel();
        model.setRowCount(0);
        try {
            List<TheThanhVien> list = theThanhVienDAO.selectTheTVHetHan();
            for (TheThanhVien ttv : list) {
                Object[] rowdata = new Object[]{
                    ttv.getMaTheTV(),
                    ttv.getTenKH(),
                    ttv.isGioiTinh() ? "Nam" : "Nữ",
                    ttv.getNgaySinh(),
                    ttv.getSDT(),
                    ttv.getCMND(),
                    ttv.getEmail(),
                    ttv.getNgayTao(),
                    ttv.getNgayHetHan()

                };
                model.addRow(rowdata);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    boolean ValidateForm() {
        StringBuilder sb = new StringBuilder();
        Validator.isNull(txtMaTheTV, "Không được để trống mã thẻ", sb);
        Validator.isNull(txtHoTen, "Không được để trống hoten", sb);
        Validator.isNull(txtSDT, "Không được để trống sdt ", sb);
        Validator.isNull(txtCMND, "Không được để trống CMND", sb);
        Validator.isNull(txtEmail, "Không được để trống Email", sb);
        Validator.checkEmail(txtEmail, sb);
        Validator.checkNgaySinh(txtNgaySinh, sb);
        Validator.checkHanSD(txtNgayHetHan, sb);

        if (sb.length() > 0) {
            MsgBox.alert(this, sb.toString());
            return false;
        }

        return true;
    }

    private boolean checkTrungMaTheTV() {
        String mattv = txtMaTheTV.getText();
        TheThanhVien theThanhVien = theThanhVienDAO.selectById(mattv);
        if (theThanhVien == null) {
            txtMaTheTV.setBackground(Color.white);
            return true;
        } else {
            txtMaTheTV.setBackground(Color.red);
            MsgBox.alert(this, "Trùng mã thẻ thành viên");
            return false;
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
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        lblAnh = new javax.swing.JLabel();
        txtMaTheTV = new javax.swing.JTextField();
        txtHoTen = new javax.swing.JTextField();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        txtNgaySinh = new com.toedter.calendar.JDateChooser();
        txtSDT = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtCMND = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtNote = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        btnThem = new javax.swing.JButton();
        btnCapNhat = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        btnWebcam = new javax.swing.JButton();
        btnChonAnh = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTheThanhVien = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        txtNgayHetHan = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        txtNgayTao = new com.toedter.calendar.JDateChooser();
        jPanel7 = new javax.swing.JPanel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtDiem = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblTheTVHetHan = new javax.swing.JTable();
        btnGiaHan = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(107, 185, 240));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Quản lý thẻ thành viên");

        jPanel2.setBackground(new java.awt.Color(107, 185, 240));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));

        jPanel3.setBackground(new java.awt.Color(107, 185, 240));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Mã thẻ:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Họ tên khách hàng:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Giới tính:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Ngày sinh:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Số điện thoại:");

        jPanel5.setBackground(new java.awt.Color(107, 185, 240));
        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));

        lblAnh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnh, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        buttonGroup1.add(rdoNam);
        rdoNam.setText("Nam");

        buttonGroup1.add(rdoNu);
        rdoNu.setSelected(true);
        rdoNu.setText("Nữ");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("CMND:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setText("Email:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setText("Ghi chú:");

        txtNote.setColumns(20);
        txtNote.setRows(5);
        jScrollPane1.setViewportView(txtNote);

        jPanel6.setBackground(new java.awt.Color(107, 185, 240));

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
        jPanel6.add(btnThem);

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
        jPanel6.add(btnCapNhat);

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
        jPanel6.add(btnMoi);

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
        jPanel6.add(btnWebcam);

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
        jPanel6.add(btnChonAnh);

        tblTheThanhVien.setModel(new javax.swing.table.DefaultTableModel(
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
        tblTheThanhVien.setRowHeight(25);
        tblTheThanhVien.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblTheThanhVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTheThanhVienMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblTheThanhVien);

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setText("Ngày tạo:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("Ngày hết hạn:");

        txtNgayTao.setEnabled(false);

        jPanel7.setBackground(new java.awt.Color(107, 185, 240));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N

        btnTimKiem.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Search.png"))); // NOI18N
        btnTimKiem.setText("Tìm kiếm");

        jLabel4.setText("Nhập từ khóa cần tìm");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(txtTimKiem)
                .addGap(18, 18, 18)
                .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTimKiem)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnTimKiem, txtTimKiem});

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setText("Điểm:");

        txtDiem.setEditable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9))
                                .addGap(38, 38, 38)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(rdoNam)
                                        .addGap(18, 18, 18)
                                        .addComponent(rdoNu)
                                        .addGap(0, 90, Short.MAX_VALUE))
                                    .addComponent(txtHoTen)
                                    .addComponent(txtNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtMaTheTV)
                                    .addComponent(txtSDT, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12))
                                .addGap(117, 117, 117)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCMND)
                                    .addComponent(txtEmail)
                                    .addComponent(txtDiem))))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel14)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel15))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNgayHetHan, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane1))
                        .addGap(110, 110, 110)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(82, 82, 82))
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(txtMaTheTV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtNgayHetHan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(rdoNam)
                            .addComponent(rdoNu)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(txtCMND, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane1)))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtDiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Danh sách", jPanel3);

        jPanel4.setBackground(new java.awt.Color(107, 185, 240));

        tblTheTVHetHan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã thẻ", "Họ tên", "Giới tính", "Ngày sinh", "Số điện thoại", "CMND", "Email", "Ngày tạo", "Ngày hết hạn"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTheTVHetHan.setRowHeight(25);
        tblTheTVHetHan.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(tblTheTVHetHan);

        btnGiaHan.setBackground(new java.awt.Color(255, 102, 102));
        btnGiaHan.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnGiaHan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/milkyway/Icons/Alarm.png"))); // NOI18N
        btnGiaHan.setText("Gia hạn");
        btnGiaHan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGiaHan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGiaHanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1162, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnGiaHan)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnGiaHan)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Đã hết hạn", jPanel4);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
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

    private void btnWebcamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWebcamActionPerformed
        WebcamUtils.chupAnh("TheThanhVien");
    }//GEN-LAST:event_btnWebcamActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        try {
            if (!ValidateForm()) {
                return;
            }
            if (checkTrungMaTheTV()) {
                return;
            }
            insert();
            fillTableTheTVConHan();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        try {
            if (!ValidateForm()) {
                return;
            }
            update();
            fillTableTheTVConHan();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        clearForm();
    }//GEN-LAST:event_btnMoiActionPerformed

    private void btnChonAnhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonAnhActionPerformed
        try {
            ImageUtils.openAndInsertHinhAnh("TheThanhVien", lblAnh);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnChonAnhActionPerformed

    private void tblTheThanhVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTheThanhVienMouseClicked
        try {
            rowConHan = tblTheThanhVien.getSelectedRow();
            this.edit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblTheThanhVienMouseClicked

    private void btnGiaHanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGiaHanActionPerformed
        if (!MsgBox.confirm(this, "Bạn có muốn gia hạn các thẻ thành viên không?")) {
            return;
        }
        try {
            StringBuilder sb = new StringBuilder();
            
            for (int i = 0; i < tblTheTVHetHan.getRowCount(); i++) {
                Validator.checkNgayHetHan(XDate.toDate(tblTheTVHetHan.getValueAt(i, 8).toString(), "dd-MM-yyyy"), sb);
                if (sb.length() > 0) {
                    MsgBox.alert(this, sb.toString());
                    return;
                }
                theThanhVienDAO.updateGiaHan(tblTheTVHetHan.getValueAt(i, 0).toString(), tblTheTVHetHan.getValueAt(i, 8).toString());
            }
            fillTabTheHetHan();
            fillTableTheTVConHan();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnGiaHanActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnChonAnh;
    private javax.swing.JButton btnGiaHan;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnWebcam;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAnh;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JTable tblTheTVHetHan;
    private javax.swing.JTable tblTheThanhVien;
    private javax.swing.JTextField txtCMND;
    private javax.swing.JTextField txtDiem;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMaTheTV;
    private com.toedter.calendar.JDateChooser txtNgayHetHan;
    private com.toedter.calendar.JDateChooser txtNgaySinh;
    private com.toedter.calendar.JDateChooser txtNgayTao;
    private javax.swing.JTextArea txtNote;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
