package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFish extends ModelBase {

    public ModelRenderer Body;
    public ModelRenderer Tail;
    public boolean inWater;

    public ModelFish() {
        this.setTextureOffset("Body.Body", 0, 0);
        this.setTextureOffset("Tail.Tail", 0, 0);
        (this.Body = new ModelRenderer((ModelBase) this, "Body")).setRotationPoint(0.0f, 16.0f, 0.0f);
        this.Body.addBox("Body", 0.0f, 0.0f, 0.0f, 0, 1, 1);
        (this.Tail = new ModelRenderer((ModelBase) this, "Tail")).setRotationPoint(0.0f, 0.0f, -1.0f);
        this.Tail.addBox("Tail", 0.0f, 0.0f, 0.0f, 0, 1, 1);
        this.Body.addChild(this.Tail);
    }

    public void render(final Entity entity, final float f, final float f1, final float f2, final float f3,
        final float f4, final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, null);
        this.Body.render(f5);
    }

    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4,
        final float f5, final Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
        if (!this.inWater) {
            this.Tail.rotateAngleY = (float) Math.sin(f2 * 0.55f) * 0.26f;
        } else {
            this.Tail.rotateAngleY = (float) Math.sin(f2 * 0.25f) * 0.25f;
        }
    }
}
