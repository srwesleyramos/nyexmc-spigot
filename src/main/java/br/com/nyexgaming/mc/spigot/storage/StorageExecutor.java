package br.com.nyexgaming.mc.spigot.storage;

import br.com.nyexgaming.mc.spigot.database.models.DeliveryModel;
import br.com.nyexgaming.mc.spigot.service.Service;
import br.com.nyexgaming.mc.spigot.service.ServiceExecutor;

public class StorageExecutor extends ServiceExecutor {

    public StorageExecutor(Service service) {
        super(service);
    }

    @Override
    public void execute(DeliveryModel[] transactions) {
        /*for (DeliveryModel shopping : transactions) {
            if (!views.containsKey(shopping.target().toLowerCase())) {
                views.put(shopping.target().toLowerCase(), new ProductsView(shopping.target(), this));
            }

            if (!products.containsKey(shopping.id_transacao)) {
                products.put(shopping.id_transacao, shopping);
                continue;
            }

            DeliveryModel memory = products.get(shopping.id_transacao);

            if (shopping.equals(memory)) {
                continue;
            }

            for (Field field : DeliveryModel.class.getFields()) {
                if (memory.entregue && field.getName().equals("entregue")) {
                    continue;
                }

                if (memory.donated && (field.getName().equals("donated") || field.getName().equals("recipient"))) {
                    continue;
                }

                try {
                    field.setAccessible(true);
                    field.set(memory, field.get(shopping));
                } catch (IllegalAccessException ignored) {
                }
            }

            products.put(shopping.id_transacao, memory);
        }

        for (ProductsView view : views.values()) {
            view.update();
        }*/
    }
}
