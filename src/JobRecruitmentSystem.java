import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

// Theme Manager Class
class ThemeManager {
    // Colors
    public static final Color PRIMARY_COLOR = new Color(0x1d, 0x35, 0x57);       // #1d3557
    public static final Color SECONDARY_COLOR = new Color(0xf1, 0xfa, 0xee);     // #f1faee
    public static final Color TEXT_COLOR = new Color(0x00, 0x15, 0x24);          // #001524
    public static final Color BACKGROUND_COLOR = new Color(0xa8, 0xda, 0xdc);    // #a8dadc


    // Fonts
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    public static final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 18);
    public static final Font BODY_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    // Button styling
    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(SECONDARY_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 100, 150)),
                BorderFactory.createEmptyBorder(8, 25, 8, 25)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(90, 150, 200));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    // Panel styling
    public static JPanel createStyledPanel(boolean withBackground) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (withBackground) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(255, 255, 255, 200));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                }
            }
        };
        panel.setOpaque(!withBackground);
        return panel;
    }

    // Text field styling
    public static JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(BODY_FONT);
        field.setMaximumSize(new Dimension(300, 30));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    // Label styling
    public static JLabel createStyledLabel(String text, boolean isTitle) {
        JLabel label = new JLabel(text);
        label.setFont(isTitle ? TITLE_FONT : BODY_FONT);
        label.setForeground(isTitle ? PRIMARY_COLOR : TEXT_COLOR);
        return label;
    }

}

// Base User Class
class User {
    protected int userId;
    protected String name, email, phoneNumber, role;

    public User(int userId, String name, String email, String phoneNumber, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public void login() {
        System.out.println(name + " logged in as " + role);
    }

    public void logout() {
        System.out.println(name + " logged out.");
    }
}

// Main Execution
public class JobRecruitmentSystem {
    public static void main(String[] args) {
        new JobRecruitmentUI();
    }
}

// UI + Logic
class JobRecruitmentUI {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTextField emailField, passwordField;
    private Connection conn;
    private String loggedInUserEmail;

    public JobRecruitmentUI() {
        initializeDB();
        initializeUI();
    }

    private void initializeDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/job_recruitment", "root", "HSSDataBase22");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Connection Failed: " + e.getMessage());
        }
    }

    private void initializeUI() {
        frame = new JFrame("Job Recruitment System");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(ThemeManager.BACKGROUND_COLOR);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(ThemeManager.BACKGROUND_COLOR);

        JPanel loginPanel = createLoginPanel();
        mainPanel.add(loginPanel, "Login");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel backgroundPanel = ThemeManager.createStyledPanel(false);
        backgroundPanel.setLayout(new BorderLayout());

        // ===== Combined logo + form panel =====
        JPanel combinedPanel = new JPanel();
        combinedPanel.setOpaque(false);
        combinedPanel.setLayout(new BoxLayout(combinedPanel, BoxLayout.Y_AXIS));

        // Logo panel (tight spacing)
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setLayout(new BorderLayout());
        logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // 0 gap

        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("resources/logo.png"));
            Image scaledLogo = logoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
            logoLabel.setHorizontalAlignment(JLabel.CENTER);
            logoPanel.add(logoLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error loading logo: " + e.getMessage());
        }

        // Login form panel
        JPanel formPanel = ThemeManager.createStyledPanel(true);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40));
        formPanel.setMaximumSize(new Dimension(400, 500));

        JLabel titleLabel = ThemeManager.createStyledLabel("LOGIN", true);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Ensure center alignment visually

        emailField = ThemeManager.createStyledTextField(20);
        emailField.setMaximumSize(new Dimension(300, 30));
        emailField.setPreferredSize(new Dimension(300, 30));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JPasswordField(20);
        passwordField.setFont(ThemeManager.BODY_FONT);
        passwordField.setMaximumSize(new Dimension(300, 30));
        passwordField.setPreferredSize(new Dimension(300, 30));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton userLoginButton = ThemeManager.createStyledButton("User Login");
        userLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        userLoginButton.addActionListener(e -> authenticateUser("JobSeeker"));

        JButton recruiterLoginButton = ThemeManager.createStyledButton("Recruiter Login");
        recruiterLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        recruiterLoginButton.addActionListener(e -> authenticateUser("Employer"));

        JButton registerButton = ThemeManager.createStyledButton("Register");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.addActionListener(e -> registerUser());

        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(20));

        // Custom layout for email
        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.Y_AXIS));
        emailPanel.setOpaque(false);
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailPanel.add(emailLabel);
        emailPanel.add(Box.createVerticalStrut(5));
        emailPanel.add(emailField);

        // Custom layout for password
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.setOpaque(false);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(Box.createVerticalStrut(5));
        passwordPanel.add(passwordField);

        formPanel.add(emailPanel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(passwordPanel);
        formPanel.add(Box.createVerticalStrut(25));

        formPanel.add(userLoginButton);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(recruiterLoginButton);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(registerButton);

        // Add logo and form to combined panel
        combinedPanel.add(logoPanel);
        combinedPanel.add(Box.createVerticalStrut(5)); // Tiny spacing
        combinedPanel.add(formPanel);

        // ===== Center combined panel on screen =====
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(combinedPanel, gbc);

        backgroundPanel.add(centerPanel, BorderLayout.CENTER);

        return backgroundPanel;
    }

    private JPanel createLabeledField(String label, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);
        JLabel jLabel = ThemeManager.createStyledLabel(label, false);
        jLabel.setPreferredSize(new Dimension(100, 20));
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(jLabel);
        panel.add(field);
        return panel;
    }

    private void authenticateUser(String role) {
        try {
            String email = emailField.getText();
            String password = new String(((JPasswordField) passwordField).getPassword());
            String sql = "SELECT * FROM users WHERE email=? AND password=? AND role=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                loggedInUserEmail = email;
                JOptionPane.showMessageDialog(frame, "Login Successful as " + role);
                showDashboard(role);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Credentials or Incorrect Role");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void registerUser() {
        JPanel registerPanel = ThemeManager.createStyledPanel(true);
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
        registerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = ThemeManager.createStyledLabel("REGISTER", true);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nameField = ThemeManager.createStyledTextField(20);
        JTextField emailField = ThemeManager.createStyledTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(ThemeManager.BODY_FONT);
        passwordField.setMaximumSize(new Dimension(300, 30));

        // Add phone number field
        JTextField phoneField = ThemeManager.createStyledTextField(20);
        phoneField.setFont(ThemeManager.BODY_FONT);

        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"JobSeeker", "Employer"});
        roleCombo.setFont(ThemeManager.BODY_FONT);
        roleCombo.setMaximumSize(new Dimension(300, 30));

        JButton submitButton = ThemeManager.createStyledButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String phone = phoneField.getText(); // Get phone number
                String role = (String) roleCombo.getSelectedItem();

                // Validate phone number (basic validation)
                if (!phone.matches("\\d{10}")) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid 10-digit phone number");
                    return;
                }

                // Update SQL to include phone number
                String sql = "INSERT INTO users (name, email, password, phoneNumber, role) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, password);
                pstmt.setString(4, phone);
                pstmt.setString(5, role);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Registration Successful");
                cardLayout.show(mainPanel, "Login");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        JButton cancelButton = ThemeManager.createStyledButton("Cancel");
        cancelButton.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        registerPanel.add(titleLabel);
        registerPanel.add(Box.createVerticalStrut(20));
        registerPanel.add(createLabeledField("Name:", nameField));
        registerPanel.add(Box.createVerticalStrut(15));
        registerPanel.add(createLabeledField("Email:", emailField));
        registerPanel.add(Box.createVerticalStrut(15));
        registerPanel.add(createLabeledField("Password:", passwordField));
        registerPanel.add(Box.createVerticalStrut(15));
        registerPanel.add(createLabeledField("Phone:", phoneField)); // Add phone field
        registerPanel.add(Box.createVerticalStrut(15));
        registerPanel.add(createLabeledField("Role:", roleCombo));
        registerPanel.add(Box.createVerticalStrut(25));
        registerPanel.add(submitButton);
        registerPanel.add(Box.createVerticalStrut(10));
        registerPanel.add(cancelButton);

        mainPanel.add(registerPanel, "Register");
        cardLayout.show(mainPanel, "Register");
    }

    private void showDashboard(String role) {
        JPanel dashboard = ThemeManager.createStyledPanel(true);
        dashboard.setLayout(new BoxLayout(dashboard, BoxLayout.Y_AXIS));
        dashboard.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel welcomeLabel = ThemeManager.createStyledLabel("Welcome, " + role, true);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backButton = ThemeManager.createStyledButton("Logout");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        dashboard.add(welcomeLabel);
        dashboard.add(Box.createVerticalStrut(30));

        if (role.equals("Employer")) {
            JButton postJobButton = ThemeManager.createStyledButton("Post Job");
            postJobButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            postJobButton.addActionListener(e -> postJob());

            JButton viewApplicationsButton = ThemeManager.createStyledButton("View Applications");
            viewApplicationsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            viewApplicationsButton.addActionListener(e -> viewApplications());

            dashboard.add(postJobButton);
            dashboard.add(Box.createVerticalStrut(15));
            dashboard.add(viewApplicationsButton);
        }
        else if (role.equals("JobSeeker")) {
            JButton searchJobButton = ThemeManager.createStyledButton("Search Jobs");
            searchJobButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            searchJobButton.addActionListener(e -> searchJobs());

            JButton viewStatusButton = ThemeManager.createStyledButton("View Application Status");
            viewStatusButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            viewStatusButton.addActionListener(e -> viewApplicationStatus());

            dashboard.add(searchJobButton);
            dashboard.add(Box.createVerticalStrut(15));
            dashboard.add(viewStatusButton);
        }

        dashboard.add(Box.createVerticalStrut(30));
        dashboard.add(backButton);

        mainPanel.add(dashboard, "Dashboard");
        cardLayout.show(mainPanel, "Dashboard");
    }

    private void postJob() {
        JPanel postJobPanel = ThemeManager.createStyledPanel(true);
        postJobPanel.setLayout(new BoxLayout(postJobPanel, BoxLayout.Y_AXIS));
        postJobPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = ThemeManager.createStyledLabel("Post New Job", true);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField titleField = ThemeManager.createStyledTextField(20);
        JTextArea descArea = new JTextArea(5, 20);
        descArea.setFont(ThemeManager.BODY_FONT);
        descArea.setLineWrap(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        JTextField salaryField = ThemeManager.createStyledTextField(20);

        JButton submitButton = ThemeManager.createStyledButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                String title = titleField.getText();
                String description = descArea.getText();
                double salary = Double.parseDouble(salaryField.getText());

                String sql = "INSERT INTO jobs (title, description, salary) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, title);
                pstmt.setString(2, description);
                pstmt.setDouble(3, salary);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Job Posted Successfully");
                cardLayout.show(mainPanel, "Dashboard");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        JButton cancelButton = ThemeManager.createStyledButton("Cancel");
        cancelButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));

        postJobPanel.add(titleLabel);
        postJobPanel.add(Box.createVerticalStrut(20));
        postJobPanel.add(createLabeledField("Job Title:", titleField));
        postJobPanel.add(Box.createVerticalStrut(15));
        postJobPanel.add(createLabeledField("Description:", descScroll));
        postJobPanel.add(Box.createVerticalStrut(15));
        postJobPanel.add(createLabeledField("Salary:", salaryField));
        postJobPanel.add(Box.createVerticalStrut(25));
        postJobPanel.add(submitButton);
        postJobPanel.add(Box.createVerticalStrut(10));
        postJobPanel.add(cancelButton);

        mainPanel.add(postJobPanel, "PostJob");
        cardLayout.show(mainPanel, "PostJob");
    }

    private void searchJobs() {
        JPanel searchPanel = ThemeManager.createStyledPanel(true);
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = ThemeManager.createStyledLabel("Available Jobs", true);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Table with non-editable cells
        String[] columnNames = {"Job ID", "Title", "Salary"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        JTable jobTable = new JTable(model);
        jobTable.setFont(ThemeManager.BODY_FONT);
        jobTable.setRowHeight(25);
        jobTable.getTableHeader().setFont(ThemeManager.BODY_FONT);
        JScrollPane scrollPane = new JScrollPane(jobTable);

        JButton refreshButton = ThemeManager.createStyledButton("Refresh");
        refreshButton.addActionListener(e -> {
            try {
                model.setRowCount(0); // Clear existing rows
                String sql = "SELECT * FROM jobs";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    Object[] row = {
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getDouble("salary")
                    };
                    model.addRow(row);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        JButton applyButton = ThemeManager.createStyledButton("Apply");
        applyButton.addActionListener(e -> {
            int selectedRow = jobTable.getSelectedRow();
            if (selectedRow != -1) {
                int jobId = (int) model.getValueAt(selectedRow, 0); // Get Job ID

                try {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Upload Resume (PDF or DOC)");
                    int result = fileChooser.showOpenDialog(frame);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();

                        int userId = getUserId(loggedInUserEmail);
                        if (userId == -1) {
                            JOptionPane.showMessageDialog(frame, "Error: User not found!");
                            return;
                        }

                        String sql = "INSERT INTO applications (jobId, jobseekerId, status, appliedDate, resumepath) VALUES (?, ?, ?, NOW(), ?)";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setInt(1, jobId);
                        pstmt.setInt(2, userId);
                        pstmt.setString(3, "Pending");

                        // Read file as binary stream
                        FileInputStream fis = new FileInputStream(selectedFile);
                        pstmt.setBinaryStream(4, fis, (int) selectedFile.length());

                        pstmt.executeUpdate();
                        JOptionPane.showMessageDialog(frame, "Applied Successfully with Resume!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a job from the table.");
            }

    });

        JButton backButton = ThemeManager.createStyledButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));

        searchPanel.add(titleLabel);
        searchPanel.add(Box.createVerticalStrut(20));
        searchPanel.add(scrollPane);
        searchPanel.add(Box.createVerticalStrut(15));
        searchPanel.add(refreshButton);
        searchPanel.add(Box.createVerticalStrut(5));
        searchPanel.add(applyButton);
        searchPanel.add(Box.createVerticalStrut(15));
        searchPanel.add(backButton);

        mainPanel.add(searchPanel, "SearchJobs");
        cardLayout.show(mainPanel, "SearchJobs");
        refreshButton.doClick(); // Load jobs immediately
    }

    private void viewApplications() {
        JPanel viewPanel = ThemeManager.createStyledPanel(true);
        viewPanel.setLayout(new BorderLayout(10, 10));
        viewPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = ThemeManager.createStyledLabel("Job Applications", true);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        viewPanel.add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"AppID", "Job Title", "Applicant", "Status", "Date"};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        JTable table = new JTable(model);
        table.setFont(ThemeManager.BODY_FONT);
        table.setRowHeight(24);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        viewPanel.add(scrollPane, BorderLayout.CENTER);

        // Refresh Button Logic
        JButton refreshButton = ThemeManager.createStyledButton("Refresh");
        refreshButton.addActionListener(e -> {
            try {
                model.setRowCount(0); // Clear previous data
                String sql = "SELECT a.id, j.title, u.name AS applicant, a.status, a.appliedDate " +
                        "FROM applications a " +
                        "JOIN jobs j ON a.jobId = j.id " +
                        "JOIN users u ON a.jobSeekerId = u.id";

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("applicant"),
                            rs.getString("status"),
                            rs.getString("appliedDate"),
//                            rs.getString("resumePath") // Add resume path here
                    });

                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        // Select Candidate Logic
        JButton selectButton = ThemeManager.createStyledButton("Select Candidate");
        selectButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int appId = (int) model.getValueAt(selectedRow, 0);
                try {
                    String sql = "UPDATE applications SET status = 'Selected' WHERE id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, appId);
                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(frame, "Candidate Selected Successfully!");
                        refreshButton.doClick();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to update status.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a row.");
            }
        });

        // Delete Row Logic
        JButton deleteButton = ThemeManager.createStyledButton("Delete Application");
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int appId = (int) model.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to delete Application ID: " + appId + "?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        String sql = "DELETE FROM applications WHERE id = ?";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setInt(1, appId);
                        int rowsAffected = pstmt.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(frame, "Application Deleted Successfully!");
                            refreshButton.doClick();
                        } else {
                            JOptionPane.showMessageDialog(frame, "Failed to delete application.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a row to delete.");
            }
        });
        JButton downloadButton = ThemeManager.createStyledButton("Download Resume");
        downloadButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int appId = (int) model.getValueAt(selectedRow, 0); // Assuming column 0 is application ID

                try {
                    String sql = "SELECT resumepath FROM applications WHERE id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, appId);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        byte[] resumeData = rs.getBytes("resumepath");

                        if (resumeData != null && resumeData.length > 0) {
                            JFileChooser fileChooser = new JFileChooser();
                            fileChooser.setDialogTitle("Save Resume");
                            int userSelection = fileChooser.showSaveDialog(frame);

                            if (userSelection == JFileChooser.APPROVE_OPTION) {
                                File fileToSave = fileChooser.getSelectedFile();
                                try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                                    fos.write(resumeData);
                                }

                                JOptionPane.showMessageDialog(frame, "Resume downloaded successfully!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "No resume found for this application.");
                        }
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }

            } else {
                JOptionPane.showMessageDialog(frame, "Please select a row to download resume.");
            }
        });




        JButton backButton = ThemeManager.createStyledButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.add(refreshButton);
        buttonPanel.add(selectButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(downloadButton);
        buttonPanel.add(backButton);
        viewPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(viewPanel, "ViewApplications");
        cardLayout.show(mainPanel, "ViewApplications");
        refreshButton.doClick(); // Load data on display
    }

    private void viewApplicationStatus() {
        JPanel statusPanel = ThemeManager.createStyledPanel(true);
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = ThemeManager.createStyledLabel("Your Applications", true);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Table setup
        String[] columnNames = {"Job Title", "Status", "Applied Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make every cell non-editable
            }
        };

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setFont(ThemeManager.BODY_FONT);
        table.setGridColor(Color.GRAY);
        table.setShowGrid(true);
        table.getTableHeader().setFont(ThemeManager.BODY_FONT);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton refreshButton = ThemeManager.createStyledButton("Refresh");
        refreshButton.addActionListener(e -> {
            try {
                int userId = getUserId(loggedInUserEmail);
                if (userId == -1) {
                    JOptionPane.showMessageDialog(frame, "Error: User not found!");
                    return;
                }

                // Clear existing data
                tableModel.setRowCount(0);

                String sql = "SELECT j.title, a.status, a.appliedDate " +
                        "FROM applications a " +
                        "JOIN jobs j ON a.jobId = j.id " +
                        "WHERE a.jobSeekerId = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();

                boolean hasApplications = false;
                while (rs.next()) {
                    hasApplications = true;
                    String title = rs.getString("title");
                    String status = rs.getString("status");
                    String date = rs.getString("appliedDate");

                    tableModel.addRow(new Object[]{title, status, date});
                }

                if (!hasApplications) {
                    JOptionPane.showMessageDialog(frame, "You haven't applied for any jobs yet.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        JButton backButton = ThemeManager.createStyledButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));

        statusPanel.add(titleLabel);
        statusPanel.add(Box.createVerticalStrut(20));
        statusPanel.add(scrollPane);
        statusPanel.add(Box.createVerticalStrut(20));
        statusPanel.add(refreshButton);
        statusPanel.add(Box.createVerticalStrut(15));
        statusPanel.add(backButton);

        mainPanel.add(statusPanel, "ViewStatus");
        cardLayout.show(mainPanel, "ViewStatus");
        refreshButton.doClick(); // Load status immediately
    }

    private int getUserId(String email) throws SQLException {
        String sql = "SELECT id FROM users WHERE email = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        return rs.next() ? rs.getInt("id") : -1;
    }
}
