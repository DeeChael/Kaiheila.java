package net.deechael.khl.entity;

import com.fasterxml.jackson.databind.JsonNode;
import net.deechael.khl.api.Game;

import java.util.ArrayList;
import java.util.List;

public class GameEntity implements Game {

    private final int id;
    private final String name;
    private final Type type;
    private final String options;
    private final String[] processName;
    private final String[] productName;
    private final String icon;

    public GameEntity(JsonNode node) {
        this.id = node.get("id").asInt();
        this.name = node.get("name").asText();
        this.type = Type.valueOf(node.get("type").asInt());
        this.options = node.get("options").asText();
        List<String> processName = new ArrayList<>();
        node.get("process_name").elements().forEachRemaining(processNameObject -> {
            processName.add(processNameObject.asText());
        });
        List<String> productName = new ArrayList<>();
        node.get("process_name").elements().forEachRemaining(productNameObject -> {
            productName.add(productNameObject.asText());
        });
        this.processName = processName.toArray(new String[0]);
        this.productName = productName.toArray(new String[0]);
        this.icon = node.get("icon").asText();
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public String getOptions() {
        return this.options;
    }

    @Override
    public String[] getProcessName() {
        return this.processName;
    }

    @Override
    public String[] getProductName() {
        return this.productName;
    }

    @Override
    public String getIcon() {
        return this.icon;
    }

}
