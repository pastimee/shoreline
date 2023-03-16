package com.momentum.impl.events.vanilla.renderer.entity;

import com.momentum.api.event.Event;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

/**
 * Called when the model base is rendered
 *
 * @author linus
 * @since 03/12/2023
 */
public class RenderModelEvent extends Event {

    // model render info
    private final ModelBase modelBase;
    private final EntityLivingBase entityLivingBase;
    private final float limbSwing;
    private final float limbSwingAmount;
    private final float ageInTicks;
    private final float netHeadYaw;
    private final float headPitch;
    private final float scaleFactor;

    /**
     * Initializes the original render info
     */
    public RenderModelEvent(ModelBase modelBase, EntityLivingBase entityLivingBase, float limbSwing,
                            float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {

        // init info
        this.modelBase = modelBase;
        this.entityLivingBase = entityLivingBase;
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
    public EntityLivingBase getEntityLivingBase() {
        return entityLivingBase;
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
