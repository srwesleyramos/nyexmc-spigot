package br.com.nyexgaming.mc.spigot.database.adapter;

import br.com.nyexgaming.mc.spigot.database.models.DeliveryModel;
import br.com.nyexgaming.sdk.endpoints.products.Product;
import com.google.gson.Gson;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliveryAdapter {

    public static DeliveryModel read(ResultSet result) throws SQLException {
        Product[] products = new Gson().fromJson(result.getString("produtos"), Product[].class);

        return new DeliveryModel(
                result.getLong("id_transacao"),
                result.getLong("id_cupom"),
                result.getLong("id_loja"),
                result.getInt("status"),
                result.getInt("entregue"),
                result.getString("identificador"),
                result.getString("email"),
                result.getString("gateway"),
                result.getDouble("valor"),
                result.getString("hex_transacao"),
                result.getString("external_reference"),
                products,
                result.getLong("criado_em"),
                result.getLong("atualizado_em"),
                result.getBoolean("presente"),
                result.getString("destinatario")
        );
    }

    public static PreparedStatement write(PreparedStatement statement, DeliveryModel delivery) throws SQLException {
        String produtos = new Gson().toJson(delivery.getProdutos());

        statement.setLong(1, delivery.getId_transacao());
        statement.setLong(2, delivery.getId_cupom());
        statement.setLong(3, delivery.getId_loja());
        statement.setLong(4, delivery.getStatus());
        statement.setLong(5, delivery.getEntregue());
        statement.setString(6, delivery.getIdentificador());
        statement.setString(7, delivery.getEmail());
        statement.setString(8, delivery.getGateway());
        statement.setDouble(9, delivery.getValor());
        statement.setString(10, delivery.getHex_transacao());
        statement.setString(11, delivery.getExternal_reference());
        statement.setString(12, produtos);
        statement.setLong(13, delivery.getCriado_em());
        statement.setLong(14, delivery.getAtualizado_em());
        statement.setBoolean(15, delivery.isPresente());
        statement.setString(16, delivery.getDestinatario());

        return statement;
    }
}
