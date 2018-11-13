
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author brenocg
 */
public class LivroDAO {
    
    public static String rsPrintTable(ResultSet rs) throws SQLException{
        if(rs == null) return "";
        
        String res = "";
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) res += ",  ";
                String columnValue = rs.getString(i);
                res += columnValue + " " + rsmd.getColumnName(i);
            }
            res += "\n";
        }
        return res;
    }
    //Duas formas diferentes de printar a tabela...
    public static String printResultSet(ResultSet rs) throws SQLException {
        
        if(rs == null) return "";
        
        String resultado = "";
        // Prepare metadata object and get the number of columns.
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        // Print column names (a header).
        for (int i = 1; i <= columnsNumber; i++) {
            if (i > 1) resultado += " | ";
            resultado += rsmd.getColumnName(i);
        }
        resultado += "\n";
        resultado += "=======++=======++=======";
        resultado += "\n";
        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) resultado += " | ";
                resultado += rs.getString(i);
            }
            resultado += "\n";
        }
        return resultado;
    }

    public static String getLivros(String nome, String autor, String ano, String editora) throws SQLException{
        //Corrigir a data
        String sqlQuery = "select titulo, newtable.nome as Autor, `descricao-categoria` as Categoria, newtable.editora as Editora, newtable.`ano-de-lancamento` as Ano_Publicado "
                + "from tb_categoria natural join (select * from tb_livro natural join tb_autor)newtable" +
                          " where titulo=? or newtable.nome=? or `ano-de-lancamento`= ? or editora=?";
        PreparedStatement psmt = ModuloConexao.conector().prepareStatement(sqlQuery);
        
        psmt.setString(1, nome);
        psmt.setString(2, autor);
        psmt.setString(3, ano);
        psmt.setString(4, editora);
        
        ResultSet rs = null;
        
        try{
            rs = psmt.executeQuery();
        }catch (SQLException ex){
            System.out.println("erro ao executar consulta");
            System.out.println(ex);
        }
        //System.out.println(rsPrintTable(rs));
        try{
            return printResultSet(rs);
        }catch(SQLException e){
            System.out.println("erro no resultado do print");
        }
        
        psmt.close();
        
        return null;
    }
}