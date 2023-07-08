package Funcoes;

import java.sql.*;
import Connection.ConnectionClass;
import Telas.Tela_Principal;
import static java.awt.image.ImageObserver.HEIGHT;
import java.util.HashMap;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author DIOGO
 */
public class Funcoes_Sql {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    Statement st = null;

    public int consultarIdCli(int idPedido) {

        int idCli = 0;
        conexao = ConnectionClass.conector();

        String sql = "select tb_cliente.idCli as 'IDCliente',"
                + " tb_cliente.NomeCli as 'Cliente'"
                + " from tb_pedido"
                + " JOIN tb_mesa ON tb_pedido.tb_mesa_idMesa = tb_mesa.idMesa"
                + " JOIN tb_Menu ON tb_pedido.tb_Menu_idMenu = tb_Menu.idMenu"
                + " JOIN tb_cliente ON tb_pedido.tb_cliente_idCli = tb_cliente.idCli"
                + " WHERE tb_pedido.idPedido like ?";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idPedido);
            rs = pst.executeQuery();

            if (rs.next()) {
                idCli = rs.getInt("IDCliente");
            }
            System.out.println("consultaCli idCli 1: " + idCli);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Funcoes_Sql consultaCli: \n" + e);
        }
        return idCli;
    }

    public int[] consultarIdMenu(int idPedido) {
        // pega o id do prato da mesa
        int[] idMenuEQnt = new int[2];

        conexao = ConnectionClass.conector();

        System.out.println("entrei no consultarIdMenu");

        String sql = "select tb_mesa.NumMesa as 'Mesa',"
                + " tb_Menu.idMenu as 'IDComida', tb_pedido.Qnt"
                + " from tb_pedido"
                + " JOIN tb_mesa ON tb_pedido.tb_mesa_idMesa = tb_mesa.idMesa"
                + " JOIN tb_Menu ON tb_pedido.tb_Menu_idMenu = tb_Menu.idMenu"
                + " JOIN tb_cliente ON tb_pedido.tb_cliente_idCli = tb_cliente.idCli"
                + " WHERE tb_pedido.idPedido like ?";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idPedido);
            rs = pst.executeQuery();

            if (rs.next()) {
                idMenuEQnt[0] = rs.getInt("IDComida");
                idMenuEQnt[1] = rs.getInt("Qnt");
            }
            System.out.println("consultarPratos idMenu 1: " + idMenuEQnt[0]);
            System.out.println("consultarPratos Qnt 1: " + idMenuEQnt[1]);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Funcoes_Sql consultarPratos: \n" + e);
            System.out.println(e);
        }
        return idMenuEQnt;
    }

    public int consultarIdMesa(int idPedido) {
        int idMesa = 0;
        conexao = ConnectionClass.conector();

        String sql = "select tb_mesa.idMesa, tb_mesa.NumMesa as 'Mesa'"
                + " from tb_pedido"
                + " JOIN tb_mesa ON tb_pedido.tb_mesa_idMesa = tb_mesa.idMesa"
                + " JOIN tb_Menu ON tb_pedido.tb_Menu_idMenu = tb_Menu.idMenu"
                + " JOIN tb_cliente ON tb_pedido.tb_cliente_idCli = tb_cliente.idCli"
                + " WHERE tb_pedido.idPedido like ?";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idPedido);
            rs = pst.executeQuery();

            if (rs.next()) {
                idMesa = rs.getInt("idMesa");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "consultarMesa: Mesa não selecionada!");
            JOptionPane.showMessageDialog(null, e);
        }
        return idMesa;

    }

    public int consultarIdFunc() {
        // pega o id do funcionario logado
        conexao = ConnectionClass.conector();
        int idFunc = 0;

        if ("Garçom".equals(Tela_Principal.lblLogin.getText())
                && "Atendimento".equals(Tela_Principal.lblSetor.getText())
                && "Atendente".equals(Tela_Principal.lblCargo.getText())) {

            String loginPassado = JOptionPane.showInputDialog(null,
                    "Digite seu login:", "Login", JOptionPane.INFORMATION_MESSAGE);
            String senhaPassada = JOptionPane.showInputDialog(null,
                    "Digite sua senha:", "Senha", JOptionPane.INFORMATION_MESSAGE);

            // PESQUISA NO BANCO DE DADOS O LOGIN E SENHA SIGITADO PELO USUARIO
            String sql = "select * from tb_funcionario where login=? and senha=?";

            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, loginPassado);
                pst.setString(2, senhaPassada);
                // ARMAZENA OS DADOS COLETADOS DA PESQUISA
                rs = pst.executeQuery();

                if (rs.next()) {
                    // ARMAZENA NA VARIAVEL perfil O DADO DA COLUNA 11 DO REGISTRO
                    idFunc = rs.getInt(1);
                    String login = rs.getString(5);
                    String cargo = rs.getString(11);
                    String setor = rs.getString(12);

                    // CONDICIONAL QUE VERIFICA QUAL CARGO HIERARQUICO 
                    //PARA MEXER DENTRO DO SISTEMA PARA MEXER DENTRO DO SISTEMA
                    if (cargo.equals("Atendente")
                            && setor.equals("Atendimento")) {

                        JOptionPane.showMessageDialog(null,
                                "Acesso Concedido! \n"
                                + "Pedido realizado! \n"
                                + "idFunc: " + idFunc + "\n"
                                + "login: " + login,
                                "Login",
                                JOptionPane.INFORMATION_MESSAGE);

                        return idFunc;

                    }

                } else {
                    // CONDICIONAL QUE INFORMA AO USUARIO QUE O LOGIN OU SENHA ESTAO ERRADOS
                    JOptionPane.showMessageDialog(null,
                            "Login ou Senha incorreta",
                            "Acesso negado",
                            JOptionPane.INFORMATION_MESSAGE);
                    return idFunc;
                }

            } catch (Exception e) {

                JOptionPane.showMessageDialog(null,
                        "verificaLogin: \n" + e);
                System.out.println("erro: " + e);
            }

        } else {
            String sql = "select idFunc"
                    + " from tb_funcionario where login like ?"
                    + "and cargo like ? and setor like ?";

            try {

                pst = conexao.prepareStatement(sql);
                pst.setString(1, Tela_Principal.lblLogin.getText() + "%");
                pst.setString(2, Tela_Principal.lblCargo.getText() + "%");
                pst.setString(3, Tela_Principal.lblSetor.getText() + "%");
                rs = pst.executeQuery();
                if (rs.next()) {
                    idFunc = rs.getInt("idFunc");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "consultaFunc: \n"
                        + e);
            }
            return idFunc;
        }
        return idFunc;

    }

}
