package boxesbreakbacks.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class AccessorySlotProvider implements DataProvider {
    protected final FabricDataOutput dataOutput;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup;
    protected AccessorySlotProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        this.dataOutput = dataOutput;
        this.registryLookup = registryLookup;
    }
    @Override
    public CompletableFuture<?> run(DataWriter writer) {
    Set<Slot> slots = new HashSet<>();
        return this.registryLookup.thenCompose(lookup -> {
            this.generateSlots(lookup, (slot) -> {
                Objects.requireNonNull(slot);
                if (slots.contains(slot)) {
                    throw new RuntimeException("Existing translation key found - " + slot.slotName + " - Duplicate will be ignored.");
                }
                slots.add(slot);
            });
            List<CompletableFuture<?>> futures = new ArrayList<>(List.of());
            for (Slot slotEntry : slots) {
                JsonObject slotJSON = getSlotJSON(slotEntry);
                Path path = dataOutput
                        .getResolver(DataOutput.OutputType.DATA_PACK, "accessories/slot")
                        .resolveJson(Identifier.of("boxesbreakbacks", slotEntry.slotName));
                futures.add(DataProvider.writeToPath(writer, slotJSON, path));
            }
            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        });
    }

    private static @NotNull JsonObject getSlotJSON(Slot slotEntry) {
        JsonObject slotJSON = new JsonObject();
        //                "replace": false,
        //                "amount": 1,
        //                "operation": "set",
        //                "order": 1000,
        //                "icon": "accessories:gui/slot/back",
        //                "validators": "accessories:tag"
        slotJSON.addProperty("replace", slotEntry.replace);
        slotJSON.addProperty("amount", slotEntry.amount);
        slotJSON.addProperty("operation", slotEntry.operation.text);
        slotJSON.addProperty("order", slotEntry.order);
        slotJSON.addProperty("icon", slotEntry.icon.toString());
        JsonArray jsonValidators = new JsonArray();
        for (Identifier validator : slotEntry.validators())
            jsonValidators.add(validator.toString());
        slotJSON.add("validators", jsonValidators);
        return slotJSON;
    }

    public abstract void generateSlots(RegistryWrapper.WrapperLookup registryLookup, Consumer<Slot> slotConsumer);

    @Override
    public String getName() {
        return "Accessories Slot Generator";
    }
    public static enum OperationType {
        ADD("add"),
        REMOVE("remove"),
        SET("set");
        public final String text;
        OperationType(String text) {
            this.text = text;
        }
    }
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static class Builder {
        private final String slotName;
        private Optional<Boolean> replace = Optional.empty();
        private Optional<Integer> amount = Optional.empty();
        private Optional<OperationType> operation = Optional.empty();
        private Optional<Integer> order = Optional.empty();
        private Optional<Identifier> icon = Optional.empty();
        private Optional<List<Identifier>> validators = Optional.empty();
        Builder(String slotName) {
            this.slotName = slotName;
        }
        public static Builder create(String slotName) {
            Objects.requireNonNull(slotName);
            return new Builder(slotName);
        }
        public Builder replace(Boolean replace) {
            if (this.replace.isPresent())
                throw new RuntimeException("Field \"Replace\" Already Specified for Slot: " + slotName);
            Objects.requireNonNull(replace);
            this.replace = Optional.of(replace);
            return this;
        }
        public Builder amount(Integer amount) {
            if (this.amount.isPresent())
                throw new RuntimeException("Field \"Amount\" Already Specified for Slot: " + slotName);
            Objects.requireNonNull(amount);
            this.amount = Optional.of(amount);
            return this;
        }
        public Builder operation(OperationType operation) {
            if (this.operation.isPresent())
                throw new RuntimeException("Field \"Operation\" Already Specified for Slot: " + slotName);
            Objects.requireNonNull(operation);
            this.operation = Optional.of(operation);
            return this;
        }
        public Builder order(Integer order) {
            if (this.order.isPresent())
                throw new RuntimeException("Field \"Order\" Already Specified for Slot: " + slotName);
            Objects.requireNonNull(order);
            this.order = Optional.of(order);
            return this;
        }
        public Builder icon(Identifier icon) {
            if (this.icon.isPresent())
                throw new RuntimeException("Field \"Icon\" Already Specified for Slot: " + slotName);
            Objects.requireNonNull(icon);
            if (!icon.getPath().startsWith("gui/slot/" + slotName))
                throw new RuntimeException("Icon Path Is Non-Standard, You Are Responsible For What Comes Next");
            this.icon = Optional.of(icon);
            return this;
        }
        public Builder addValidator(Identifier validator) {
            if (validators.isEmpty())
                validators = Optional.of(List.of(validator));
            else
                validators.get().add(validator);
            return this;
        }
        public Builder addValidators(List<Identifier> validators) {
            Objects.requireNonNull(validators);
            validators.forEach(Objects::requireNonNull);
            if (this.validators.isEmpty())
                this.validators = Optional.of(List.copyOf(validators));
            else
                this.validators.get().addAll(validators);
            return this;
        }
        public Slot build() {
            if (replace.isEmpty())
                throw new RuntimeException("Field \"Replace\" Must Be Specified for Slot: " + slotName);
            if (amount.isEmpty())
                throw new RuntimeException("Field \"Amount\" Must Be Specified for Slot: " + slotName);
            if (operation.isEmpty())
                throw new RuntimeException("Field \"Operation\" Must Be Specified for Slot: " + slotName);
            if (order.isEmpty())
                throw new RuntimeException("Field \"Order\" Must Be Specified for Slot: " + slotName);
            if (icon.isEmpty())
                throw new RuntimeException("Field \"Amount\" Must Be Specified for Slot: " + slotName);
            if (validators.isEmpty())
                throw new RuntimeException("Field \"Validators\" Must Be Specified for Slot: " + slotName);
            return new Slot(slotName, replace.get(), amount.get(), operation.get(), order.get(), icon.get(), validators.get());
        }
    }
    public record Slot(String slotName, boolean replace, int amount, OperationType operation, int order, Identifier icon, List<Identifier> validators) {

    }
}
