package icbm.dianqi;

import icbm.ZhuYao;
import icbm.api.ICBM;

import java.util.List;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import universalelectricity.prefab.ItemElectric;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class ItGenZongQi extends ItemElectric
{
	private static final float YONG_DIAN_LIANG = 0.05f;

	public ItGenZongQi(String name, int id, int iconIndex)
	{
		super(id);
		this.setIconIndex(iconIndex);
		this.setItemName(name);
		this.setCreativeTab(ZhuYao.TAB);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		super.addInformation(itemStack, par2EntityPlayer, par3List, par4);

		Entity trackingEntity = getTrackingEntity(FMLClientHandler.instance().getClient().theWorld, itemStack);

		if (trackingEntity != null)
		{
			par3List.add("Tracking: " + trackingEntity.getEntityName());
		}
	}

	public static void setTrackingEntity(ItemStack itemStack, Entity entity)
	{
		if (itemStack.stackTagCompound == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		if (entity != null)
		{
			itemStack.stackTagCompound.setInteger("trackingEntity", entity.entityId);
		}
	}

	public static Entity getTrackingEntity(World worldObj, ItemStack itemStack)
	{
		if (worldObj != null)
		{
			if (itemStack.stackTagCompound != null)
			{
				int trackingID = itemStack.stackTagCompound.getInteger("trackingEntity");
				return worldObj.getEntityByID(trackingID);
			}
		}
		return null;
	}

	@Override
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		super.onCreated(par1ItemStack, par2World, par3EntityPlayer);
		setTrackingEntity(par1ItemStack, par3EntityPlayer);
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
	{
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);

		if (par3Entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) par3Entity;

			if (player.inventory.getCurrentItem() != null)
			{
				if (player.inventory.getCurrentItem().itemID == this.shiftedIndex)
				{
					Entity trackingEntity = ItGenZongQi.getTrackingEntity(par2World, par1ItemStack);

					if (trackingEntity != null)
					{
						this.onUse(YONG_DIAN_LIANG, par1ItemStack);

						if (this.getJoules(par1ItemStack) < YONG_DIAN_LIANG)
						{
							this.setTrackingEntity(par1ItemStack, null);
						}
					}
				}
			}
		}
	}

	/**
	 * Called when the player Left Clicks
	 * (attacks) an entity. Processed before
	 * damage is done, if return value is true
	 * further processing is canceled and the
	 * entity is not attacked.
	 * 
	 * @param itemStack
	 *            The Item being used
	 * @param player
	 *            The player that is attacking
	 * @param entity
	 *            The entity being attacked
	 * @return True to cancel the rest of the
	 *         interaction.
	 */
	public boolean onLeftClickEntity(ItemStack itemStack, EntityPlayer player, Entity entity)
	{
		if (!player.worldObj.isRemote)
		{
			if (this.getJoules(itemStack) > YONG_DIAN_LIANG)
			{
				setTrackingEntity(itemStack, entity);
				player.addChatMessage("Now tracking: " + entity.getEntityName());
				return true;
			}
			else
			{
				player.addChatMessage("Tracker out of electricity!");
			}
		}

		return false;
	}

	@Override
	public double getVoltage()
	{
		return 20;
	}

	@Override
	public String getTextureFile()
	{
		return ICBM.TRACKER_TEXTURE_FILE;
	}

	@Override
	public double getMaxJoules(Object... data)
	{
		return 100000;
	}
}
