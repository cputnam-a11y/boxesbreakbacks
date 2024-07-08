package boxesbreakbacks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Debug(export = true)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
        implements Attackable {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
//    @ModifyArgs(method = "tickFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setFlag(IZ)V"))
//    private void modifyFlyingFlag(Args args) {
//        LivingEntity $this = (LivingEntity) (Object) this;
//        if (!($this instanceof PlayerEntity player))
//            return;
//
//        if (TrinketsApi.getTrinketComponent(player).isEmpty())
//            return;
//        TrinketComponent trinketComponent = TrinketsApi.getTrinketComponent(player).get();
//        if (trinketComponent.isEquipped(Items.ELYTRA))
//            args.set(1, true);
//    }
    @ModifyExpressionValue(method = "tickFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack getEquipedElyta(ItemStack original) {
        if (original.isOf(Items.ELYTRA))
            return original;
        if (TrinketsApi.getTrinketComponent((LivingEntity) (Object) this).isEmpty())
            return original;
        TrinketComponent trinketComponent = TrinketsApi.getTrinketComponent((LivingEntity) (Object) this).get();
        if (trinketComponent.isEquipped(Items.ELYTRA))
            return trinketComponent.getEquipped(Items.ELYTRA).getFirst().getRight();

        return original;
    }
    @WrapOperation(method = "tickFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V"))
    private void handleTrinketItemDamage(ItemStack instance, int amount, LivingEntity entity, EquipmentSlot slot, Operation<Void> original) {
        if (!entity.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA) && TrinketsApi.getTrinketComponent(entity).isPresent() && TrinketsApi.getTrinketComponent(entity).get().isEquipped(Items.ELYTRA) && entity instanceof ServerPlayerEntity player) {
            TrinketComponent trinketComponent = TrinketsApi.getTrinketComponent(entity).get();
            Pair<SlotReference, ItemStack> x = trinketComponent.getEquipped((s) -> s == instance).getFirst();
            x.getRight().damage(amount, entity.getRandom(), player, () -> {
                x.getLeft().inventory().setStack(x.getLeft().index(), ItemStack.EMPTY);
            });
        }
        original.call(instance, amount, entity, slot);
    }

}
