package Telas.Mesas;

import java.sql.*;
// chama o arquivo de conexao com o banco de dados
import Connection.ConnectionClass;
import Funcoes.Funcoes_Sql;
import Telas.Tela_Principal;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.awt.Component;
import static java.awt.image.ImageObserver.HEIGHT;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author diogo e eric
 */
public class Tela_Mesa_Cad extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int counter;
    boolean isit = false;
    int setar;
    int idMesa = 0;

    /**
     * Creates new form TelaVisualizarFunc
     */
    public Tela_Mesa_Cad() {
        initComponents();
        conexao = ConnectionClass.conector();
        consultarComida();
        procedAjustaTable();
    }

    private void limpar() {
        jspNumMesa.setValue(1);
        jspNumCap.setValue(1);
        cbo_Stat.setSelectedItem("");
        AtxtObs.setText("");
        txt_IdMesa.setText("");
    }

    private void consultarComida() {

        String sql = "select idMesa as 'ID', NumMesa as 'Numero da mesa',"
                + " Capacidade, status, obs as 'Observação'"
                + " from tb_mesa"
                + " where idMesa like ?";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesquisar.getText() + "%");
            rs = pst.executeQuery();

            tblMesa.setModel(DbUtils.resultSetToTableModel(rs));
            tblMesa.getTableHeader().setReorderingAllowed(false);

        } catch (Exception e) {
            //   JOptionPane.showMessageDialog(null, e);
        }
    }

    private void procedAjustaTable() {

        //tblFunc.setGridColor(Color.RGBtoHSB(ERROR, WIDTH, ABORT, "0F0F0F"));
        tblMesa.setShowGrid(true);
        tblMesa.setRowHeight(30);

        //Define o modo de redimensionamento automático da tabela como 
        //"AUTO_RESIZE_OFF", que desabilita o redimensionamento automático da
        //largura das colunas.
        tblMesa.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int column = 0; column < tblMesa.getColumnCount(); column++) {
            TableColumn ColunaTable = tblMesa.getColumnModel().getColumn(column);
            // largura preferida da coluna(mínima)
            int miniWidth = ColunaTable.getMinWidth();
            // largura máxima da coluna.
            int maxWidth = ColunaTable.getMaxWidth();

            // Percorre cada linha da tabela para obter o conteúdo da célula.
            for (int linhas = 0; linhas < tblMesa.getRowCount(); linhas++) {
                // Obtém o renderizador(o valor ) de célula para a célula especificada.
                TableCellRenderer renderizadorCelula = tblMesa.getCellRenderer(linhas, column);
                // Prepara o renderizador para desenhar na célula especificada.
                Component c = tblMesa.prepareRenderer(renderizadorCelula, linhas, column);
                // Obtém a largura preferida do componente que representa a célula.
                int width = c.getPreferredSize().width + tblMesa.getIntercellSpacing().width;
                // Calcula a largura total da célula, que é a largura preferida (minima)
                //  + o espaçamento entre as células.
                miniWidth = Math.max(miniWidth, width);

                // Se a largura preferida(minima) da coluna for maior ou igual à largura 
                // máxima da coluna, define a largura preferida(minima) da coluna 
                // como a largura máxima da coluna.
                if (miniWidth >= maxWidth) {
                    miniWidth = maxWidth;
                    break;
                }
            }

            // Define a largura preferida(minima) da coluna como a largura da coluna atual.
            ColunaTable.setPreferredWidth(miniWidth + (30 * tblMesa.getColumnName(column).length()));
            //ColunaTable.setPreferredWidth(preferredWidth + ColunaTable.getWidth());
        }

    }

    private void cadastrar() {
        String sql = "insert into tb_mesa( NumMesa, Capacidade, status, obs)"
                + " values(?, ?, ?, ?)";

        try {

            pst = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setInt(1, Integer.parseInt(jspNumMesa.getValue().toString()));
            pst.setInt(2, Integer.parseInt(jspNumCap.getValue().toString()));
            pst.setString(3, cbo_Stat.getSelectedItem().toString());
            pst.setString(4, AtxtObs.getText());

            if ((AtxtObs.getText().trim().isEmpty())
                    || (jspNumMesa.getValue().toString().trim().isEmpty())
                    || (cbo_Stat.getSelectedItem().toString().trim().isEmpty())
                    || (jspNumCap.getValue().toString().trim().isEmpty())) {

                JOptionPane.showInternalMessageDialog(null,
                        "Preencha todos os campos obrigatórios!\n"
                                + "Numero da Mesa; \nStatus; \nCapacidade; \nObs; ");

            } else {

                int adicionado = pst.executeUpdate();
//                System.out.println(adicionado);

                if (adicionado > 0) {

                    JOptionPane.showInternalMessageDialog(null,
                            "Cadastrado com sucesso!");

                    rs = pst.getGeneratedKeys();
                    if (rs.next()) {
                        idMesa = rs.getInt(1); //idCli gerado
                        getCadastrarLogMesa(3); // insert
                        limpar();
                    }

                }

            }
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null,
                    "ERRO NO CATCH: "
                    + e);
        }
    }

    private void getCadastrarLogMesa(int i) {
        // cadastra log funcionario

        String nome = jspNumMesa.getValue().toString();
        int capacidade = (int) jspNumCap.getValue();
        String obs = AtxtObs.getText();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnPrincipal = new javax.swing.JPanel();
        pnCima = new javax.swing.JPanel();
        lblPesquisa = new javax.swing.JLabel();
        txtPesquisar = new javax.swing.JTextField();
        btnAtt = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        pnMeio = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMesa = new javax.swing.JTable();
        pnBaixo = new javax.swing.JPanel();
        lbl_Id1 = new javax.swing.JLabel();
        txt_IdMesa = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();
        cbo_Stat = new javax.swing.JComboBox<>();
        lblCapacidade = new javax.swing.JLabel();
        jspNumCap = new javax.swing.JSpinner();
        btnrCad = new javax.swing.JButton();
        lblNomeMesa = new javax.swing.JLabel();
        lblObs = new javax.swing.JLabel();
        jspObs = new javax.swing.JScrollPane();
        AtxtObs = new javax.swing.JTextArea();
        jspNumMesa = new javax.swing.JSpinner();

        setBackground(new java.awt.Color(0, 0, 0));
        setBorder(null);
        setClosable(true);
        setResizable(true);
        setTitle("Tela Cadastro Mesa");
        setToolTipText("");
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        pnPrincipal.setBackground(new java.awt.Color(153, 153, 153));
        pnPrincipal.setMinimumSize(new java.awt.Dimension(800, 600));
        pnPrincipal.setPreferredSize(new java.awt.Dimension(800, 600));
        pnPrincipal.setLayout(new java.awt.GridBagLayout());

        pnCima.setBackground(new java.awt.Color(51, 51, 51));
        pnCima.setMinimumSize(new java.awt.Dimension(50, 50));
        pnCima.setPreferredSize(new java.awt.Dimension(50, 50));
        pnCima.setLayout(new java.awt.GridBagLayout());

        lblPesquisa.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblPesquisa.setForeground(new java.awt.Color(204, 204, 204));
        lblPesquisa.setText("Pesquisar:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnCima.add(lblPesquisa, gridBagConstraints);

        txtPesquisar.setBackground(new java.awt.Color(204, 204, 204));
        txtPesquisar.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        txtPesquisar.setMinimumSize(new java.awt.Dimension(45, 45));
        txtPesquisar.setPreferredSize(new java.awt.Dimension(45, 45));
        txtPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesquisarKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnCima.add(txtPesquisar, gridBagConstraints);

        btnAtt.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnAtt.setText("Atualizar");
        btnAtt.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnAtt.setMaximumSize(new java.awt.Dimension(45, 45));
        btnAtt.setMinimumSize(new java.awt.Dimension(45, 45));
        btnAtt.setPreferredSize(new java.awt.Dimension(45, 45));
        btnAtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAttActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnCima.add(btnAtt, gridBagConstraints);

        btnLimpar.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnLimpar.setText("Limpar");
        btnLimpar.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnLimpar.setMaximumSize(new java.awt.Dimension(45, 45));
        btnLimpar.setMinimumSize(new java.awt.Dimension(45, 45));
        btnLimpar.setPreferredSize(new java.awt.Dimension(45, 45));
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnCima.add(btnLimpar, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        pnPrincipal.add(pnCima, gridBagConstraints);

        pnMeio.setBackground(new java.awt.Color(102, 102, 102));
        pnMeio.setMinimumSize(new java.awt.Dimension(50, 50));
        pnMeio.setPreferredSize(new java.awt.Dimension(50, 50));
        pnMeio.setLayout(new java.awt.GridBagLayout());

        tblMesa.setBackground(new java.awt.Color(102, 102, 102));
        tblMesa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblMesa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblMesa.setGridColor(new java.awt.Color(15, 15, 15));
        tblMesa.setRequestFocusEnabled(false);
        tblMesa.setSelectionBackground(new java.awt.Color(0, 51, 204));
        tblMesa.setSurrendersFocusOnKeystroke(true);
        tblMesa.getTableHeader().setReorderingAllowed(false);
        tblMesa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblMesaMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblMesa);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnMeio.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        pnPrincipal.add(pnMeio, gridBagConstraints);

        pnBaixo.setBackground(new java.awt.Color(51, 51, 51));
        pnBaixo.setMinimumSize(new java.awt.Dimension(50, 50));
        pnBaixo.setPreferredSize(new java.awt.Dimension(50, 50));
        pnBaixo.setLayout(new java.awt.GridBagLayout());

        lbl_Id1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lbl_Id1.setForeground(new java.awt.Color(204, 204, 204));
        lbl_Id1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lbl_Id1.setText("ID:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lbl_Id1, gridBagConstraints);

        txt_IdMesa.setBackground(new java.awt.Color(204, 204, 204));
        txt_IdMesa.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        txt_IdMesa.setEnabled(false);
        txt_IdMesa.setMinimumSize(new java.awt.Dimension(45, 45));
        txt_IdMesa.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(txt_IdMesa, gridBagConstraints);

        lblStatus.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(204, 204, 204));
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblStatus.setText("Status:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblStatus, gridBagConstraints);

        cbo_Stat.setBackground(new java.awt.Color(204, 204, 204));
        cbo_Stat.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        cbo_Stat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "Livre", "Ocupado" }));
        cbo_Stat.setBorder(null);
        cbo_Stat.setMinimumSize(new java.awt.Dimension(45, 45));
        cbo_Stat.setPreferredSize(new java.awt.Dimension(45, 45));
        cbo_Stat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_StatActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(cbo_Stat, gridBagConstraints);

        lblCapacidade.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblCapacidade.setForeground(new java.awt.Color(204, 204, 204));
        lblCapacidade.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCapacidade.setText("Capacidade:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblCapacidade, gridBagConstraints);

        jspNumCap.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jspNumCap.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        jspNumCap.setBorder(null);
        jspNumCap.setMinimumSize(new java.awt.Dimension(45, 45));
        jspNumCap.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(jspNumCap, gridBagConstraints);

        btnrCad.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnrCad.setText("Cadastro");
        btnrCad.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnrCad.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        btnrCad.setMinimumSize(new java.awt.Dimension(45, 45));
        btnrCad.setPreferredSize(new java.awt.Dimension(45, 45));
        btnrCad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnrCadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(btnrCad, gridBagConstraints);

        lblNomeMesa.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblNomeMesa.setForeground(new java.awt.Color(204, 204, 204));
        lblNomeMesa.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblNomeMesa.setText("<html>Numero <br>\nda mesa:</html>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblNomeMesa, gridBagConstraints);

        lblObs.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblObs.setForeground(new java.awt.Color(204, 204, 204));
        lblObs.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblObs.setText("Obs:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblObs, gridBagConstraints);

        jspObs.setBackground(new java.awt.Color(204, 204, 204));
        jspObs.setBorder(null);
        jspObs.setMinimumSize(new java.awt.Dimension(45, 45));
        jspObs.setPreferredSize(new java.awt.Dimension(45, 45));

        AtxtObs.setBackground(new java.awt.Color(204, 204, 204));
        AtxtObs.setColumns(5);
        AtxtObs.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        AtxtObs.setLineWrap(true);
        AtxtObs.setRows(1);
        AtxtObs.setToolTipText("");
        AtxtObs.setWrapStyleWord(true);
        AtxtObs.setBorder(null);
        AtxtObs.setMinimumSize(new java.awt.Dimension(45, 45));
        AtxtObs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                AtxtObsKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                AtxtObsKeyTyped(evt);
            }
        });
        jspObs.setViewportView(AtxtObs);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(jspObs, gridBagConstraints);

        jspNumMesa.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jspNumMesa.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        jspNumMesa.setBorder(null);
        jspNumMesa.setMinimumSize(new java.awt.Dimension(45, 45));
        jspNumMesa.setPreferredSize(new java.awt.Dimension(45, 45));
        jspNumMesa.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jspNumMesaComponentHidden(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(jspNumMesa, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnPrincipal.add(pnBaixo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(pnPrincipal, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAttActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAttActionPerformed
        consultarComida();
        procedAjustaTable();
    }//GEN-LAST:event_btnAttActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        limpar();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void tblMesaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMesaMousePressed
        tblMesa.editingCanceled(null);
        tblMesa.editingStopped(null);
    }//GEN-LAST:event_tblMesaMousePressed

    private void txtPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarKeyReleased
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        consultarComida();
    }//GEN-LAST:event_txtPesquisarKeyReleased

    private void txtPesquisarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarKeyTyped
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        consultarComida();
    }//GEN-LAST:event_txtPesquisarKeyTyped

    private void btnrCadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrCadActionPerformed
        cadastrar();
    }//GEN-LAST:event_btnrCadActionPerformed

    private void cbo_StatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_StatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_StatActionPerformed

    private void AtxtObsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtObsKeyReleased

    }//GEN-LAST:event_AtxtObsKeyReleased

    private void AtxtObsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtObsKeyTyped

    }//GEN-LAST:event_AtxtObsKeyTyped

    private void jspNumMesaComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jspNumMesaComponentHidden
        // TODO add your handling code here:
    }//GEN-LAST:event_jspNumMesaComponentHidden


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea AtxtObs;
    private javax.swing.JButton btnAtt;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnrCad;
    private javax.swing.JComboBox<String> cbo_Stat;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jspNumCap;
    private javax.swing.JSpinner jspNumMesa;
    private javax.swing.JScrollPane jspObs;
    private javax.swing.JLabel lblCapacidade;
    private javax.swing.JLabel lblNomeMesa;
    private javax.swing.JLabel lblObs;
    private javax.swing.JLabel lblPesquisa;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lbl_Id1;
    private javax.swing.JPanel pnBaixo;
    private javax.swing.JPanel pnCima;
    private javax.swing.JPanel pnMeio;
    private javax.swing.JPanel pnPrincipal;
    private javax.swing.JTable tblMesa;
    private javax.swing.JTextField txtPesquisar;
    private javax.swing.JTextField txt_IdMesa;
    // End of variables declaration//GEN-END:variables
}
