package com.momentum.impl.events.vanilla.renderer.entity;

import com.momentum.api.event.EventStage;
import com.momentum.api.event.StageEvent;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;

/**
 * Called when an ender crystal is rendered
 *
 * @author linus
 * @since 03/13/2023
 */
public class RenderEnderCrystalEvent extends StageEvent<EventStage> {

    // render info
    private final ModelBase modelBase;
    private final EntityEnderCrystal entity;
    private final float limbSwing;
    private final float limbSwingAmount;
    private final float ageInTicks;
    private final float netHeadYaw;
    private final float headPitch;
    private final float scaleFactor;

    /**
     * Initializes the original render info
     */
    public RenderEnderCrystalEvent(ModelBase modelBase, EntityEnderCrystal entity, float limbSwing,
                                 float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {

        // init info
        this.modelBase = modelBase;
        this.entity = entity;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.scaleFactor = scaleFactor;
    }

    /**
     * Gets the entity model
     *
     * @return The entity model
     */
    public ModelBase getModelBase() {
        return modelBase;
    }

    /**
     * Gets the entity being rendered
     *
     * @return The entity being rendered
     */
    public EntityEnderCrystal getEntityEnderCrystal() {
        return entity;
    }

    /**
     * Gets the entity's limb swing
     *
     * @return The entity's limb swing
     */
    public float getLimbSwing() {
        return limbSwing;
    }

    /**
     * Gets the entity's limb swing amount
     *
     * @return The entity's limb swing amount
     */
    public float getLimbSwingAmount() {
        return limbSwingAmount;
    }

    /**
     * Gets the entity's tick age
     *
     * @return The entity's tick age
     */
    public float getAgeInTicks() {
        return ageInTicks;
    }

    /**
     * Gets the entity's head yaw
     *
     * @return The entity's head yaw
     */
    public float getNetHeadYaw() {
        return netHeadYaw;
    }

    /**
     * Gets the entity's head pitch
     *
     * @return The entity's head pitch
     */
    public float getHeadPitch() {
        return headPitch;
    }

    /**
     * Gets the entity's render scale factor
     *
     * @return The entity's render scale factor
     */
    public float getScaleFactor() {
        return scaleFactor;
    }
}
