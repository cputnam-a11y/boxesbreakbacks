package boxesbreakbacks.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAccessorySlotProvider extends AccessorySlotProvider{
    public ModAccessorySlotProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateSlots(RegistryWrapper.WrapperLookup registryLookup, Consumer<Slot> slotConsumer) {
        slotConsumer.accept(new AccessorySlotProvider.Builder("back").replace(false).amount(1).order(1000).operation(OperationType.SET).icon(Identifier.of("accessories:gui/slot/back")).addValidator(Identifier.of("accessories:tag")).build());
    }
}
