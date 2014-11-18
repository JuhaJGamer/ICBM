package icbm.explosion.ex;

import icbm.Settings;
import icbm.explosion.blast.BlastSky;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import resonant.lib.recipe.RecipeUtility;

public class ExEndothermic extends Explosion
{
    public ExEndothermic()
    {
        super("endothermic", 3);
        this.modelName = "missile_endothermic.tcn";
    }

    @Override
    public void init()
    {
        RecipeUtility.addRecipe(new ShapedOreRecipe(this.getItemStack(), new Object[] { "?!?", "!@!", "?!?", '@', attractive.getItemStack(), '?', Blocks.ice, '!', Blocks.snow }), this.getUnlocalizedName(), Settings.CONFIGURATION, true);
    }

    @Override
    public void doCreateExplosion(World world, double x, double y, double z, Entity entity)
    {
        new BlastSky(world, entity, x, y, z, 50).explode();
    }
}
