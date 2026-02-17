package com.github.theredbrain.overhauleddamage.mixin.client.gui.hud;

import com.github.theredbrain.overhauleddamage.gui.hud.DuckGuiMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public class GuiMixin implements DuckGuiMixin {

	@Shadow
	private int tickCount;

	@Unique
	private int displayBleedingBuildUp;
	@Unique
	private int lastBleedingBuildUp;
	@Unique
	private long lastBleedingBuildUpTime;
	@Unique
	private long bleedingBuildUpBlinkTime;

	@Unique
	private int displayBurnBuildUp;
	@Unique
	private int lastBurnBuildUp;
	@Unique
	private long lastBurnBuildUpTime;
	@Unique
	private long burnBuildUpBlinkTime;

	@Unique
	private int displayFreezeBuildUp;
	@Unique
	private int lastFreezeBuildUp;
	@Unique
	private long lastFreezeBuildUpTime;
	@Unique
	private long freezeBuildUpBlinkTime;

	@Unique
	private int displayPoisonBuildUp;
	@Unique
	private int lastPoisonBuildUp;
	@Unique
	private long lastPoisonBuildUpTime;
	@Unique
	private long poisonBuildUpBlinkTime;

	@Unique
	private int displayShockBuildUp;
	@Unique
	private int lastShockBuildUp;
	@Unique
	private long lastShockBuildUpTime;
	@Unique
	private long shockBuildUpBlinkTime;

	@Unique
	private int displayStaggerBuildUp;
	@Unique
	private int lastStaggerBuildUp;
	@Unique
	private long lastStaggerBuildUpTime;
	@Unique
	private long staggerBuildUpBlinkTime;

	@Override
	public int overhauleddamage$getTickCount() {
		return tickCount;
	}

	@Override
	public int overhauleddamage$getDisplayBleedingBuildUp() {
		return this.displayBleedingBuildUp;
	}

	@Override
	public void overhauleddamage$setDisplayBleedingBuildUp(int displayBleedingBuildUp) {
		this.displayBleedingBuildUp = displayBleedingBuildUp;
	}

	@Override
	public int overhauleddamage$getLastBleedingBuildUp() {
		return this.lastBleedingBuildUp;
	}

	@Override
	public void overhauleddamage$setLastBleedingBuildUp(int lastBleedingBuildUp) {
		this.lastBleedingBuildUp = lastBleedingBuildUp;
	}

	@Override
	public long overhauleddamage$getLastBleedingBuildUpTime() {
		return this.lastBleedingBuildUpTime;
	}

	@Override
	public void overhauleddamage$setLastBleedingBuildUpTime(long lastBleedingBuildUpTime) {
		this.lastBleedingBuildUpTime = lastBleedingBuildUpTime;
	}

	@Override
	public long overhauleddamage$getBleedingBuildUpIconBlinkTime() {
		return this.bleedingBuildUpBlinkTime;
	}

	@Override
	public void overhauleddamage$setBleedingBuildUpIconBlinkTime(long bleedingBuildUpIconBlinkTime) {
		this.bleedingBuildUpBlinkTime = bleedingBuildUpIconBlinkTime;
	}

	@Override
	public int overhauleddamage$getDisplayBurnBuildUp() {
		return this.displayBurnBuildUp;
	}

	@Override
	public void overhauleddamage$setDisplayBurnBuildUp(int displayBurnBuildUp) {
		this.displayBurnBuildUp = displayBurnBuildUp;
	}

	@Override
	public int overhauleddamage$getLastBurnBuildUp() {
		return this.lastBurnBuildUp;
	}

	@Override
	public void overhauleddamage$setLastBurnBuildUp(int lastBurnBuildUp) {
		this.lastBurnBuildUp = lastBurnBuildUp;
	}

	@Override
	public long overhauleddamage$getLastBurnBuildUpTime() {
		return this.lastBurnBuildUpTime;
	}

	@Override
	public void overhauleddamage$setLastBurnBuildUpTime(long lastBurnBuildUpTime) {
		this.lastBurnBuildUpTime = lastBurnBuildUpTime;
	}

	@Override
	public long overhauleddamage$getBurnBuildUpIconBlinkTime() {
		return this.burnBuildUpBlinkTime;
	}

	@Override
	public void overhauleddamage$setBurnBuildUpIconBlinkTime(long burnBuildUpIconBlinkTime) {
		this.burnBuildUpBlinkTime = burnBuildUpIconBlinkTime;
	}

	@Override
	public int overhauleddamage$getDisplayFreezeBuildUp() {
		return this.displayFreezeBuildUp;
	}

	@Override
	public void overhauleddamage$setDisplayFreezeBuildUp(int displayFreezeBuildUp) {
		this.displayFreezeBuildUp = displayFreezeBuildUp;
	}

	@Override
	public int overhauleddamage$getLastFreezeBuildUp() {
		return this.lastFreezeBuildUp;
	}

	@Override
	public void overhauleddamage$setLastFreezeBuildUp(int lastFreezeBuildUp) {
		this.lastFreezeBuildUp = lastFreezeBuildUp;
	}

	@Override
	public long overhauleddamage$getLastFreezeBuildUpTime() {
		return this.lastFreezeBuildUpTime;
	}

	@Override
	public void overhauleddamage$setLastFreezeBuildUpTime(long lastFreezeBuildUpTime) {
		this.lastFreezeBuildUpTime = lastFreezeBuildUpTime;
	}

	@Override
	public long overhauleddamage$getFreezeBuildUpIconBlinkTime() {
		return this.freezeBuildUpBlinkTime;
	}

	@Override
	public void overhauleddamage$setFreezeBuildUpIconBlinkTime(long freezeBuildUpIconBlinkTime) {
		this.freezeBuildUpBlinkTime = freezeBuildUpIconBlinkTime;
	}

	@Override
	public int overhauleddamage$getDisplayPoisonBuildUp() {
		return this.displayPoisonBuildUp;
	}

	@Override
	public void overhauleddamage$setDisplayPoisonBuildUp(int displayPoisonBuildUp) {
		this.displayPoisonBuildUp = displayPoisonBuildUp;
	}

	@Override
	public int overhauleddamage$getLastPoisonBuildUp() {
		return this.lastPoisonBuildUp;
	}

	@Override
	public void overhauleddamage$setLastPoisonBuildUp(int lastPoisonBuildUp) {
		this.lastPoisonBuildUp = lastPoisonBuildUp;
	}

	@Override
	public long overhauleddamage$getLastPoisonBuildUpTime() {
		return this.lastPoisonBuildUpTime;
	}

	@Override
	public void overhauleddamage$setLastPoisonBuildUpTime(long lastPoisonBuildUpTime) {
		this.lastPoisonBuildUpTime = lastPoisonBuildUpTime;
	}

	@Override
	public long overhauleddamage$getPoisonBuildUpIconBlinkTime() {
		return this.poisonBuildUpBlinkTime;
	}

	@Override
	public void overhauleddamage$setPoisonBuildUpIconBlinkTime(long poisonBuildUpIconBlinkTime) {
		this.poisonBuildUpBlinkTime = poisonBuildUpIconBlinkTime;
	}

	@Override
	public int overhauleddamage$getDisplayShockBuildUp() {
		return this.displayShockBuildUp;
	}

	@Override
	public void overhauleddamage$setDisplayShockBuildUp(int displayShockBuildUp) {
		this.displayShockBuildUp = displayShockBuildUp;
	}

	@Override
	public int overhauleddamage$getLastShockBuildUp() {
		return this.lastShockBuildUp;
	}

	@Override
	public void overhauleddamage$setLastShockBuildUp(int lastShockBuildUp) {
		this.lastShockBuildUp = lastShockBuildUp;
	}

	@Override
	public long overhauleddamage$getLastShockBuildUpTime() {
		return this.lastShockBuildUpTime;
	}

	@Override
	public void overhauleddamage$setLastShockBuildUpTime(long lastShockBuildUpTime) {
		this.lastShockBuildUpTime = lastShockBuildUpTime;
	}

	@Override
	public long overhauleddamage$getShockBuildUpIconBlinkTime() {
		return this.shockBuildUpBlinkTime;
	}

	@Override
	public void overhauleddamage$setShockBuildUpIconBlinkTime(long shockBuildUpIconBlinkTime) {
		this.shockBuildUpBlinkTime = shockBuildUpIconBlinkTime;
	}

	@Override
	public int overhauleddamage$getDisplayStaggerBuildUp() {
		return this.displayStaggerBuildUp;
	}

	@Override
	public void overhauleddamage$setDisplayStaggerBuildUp(int displayStaggerBuildUp) {
		this.displayStaggerBuildUp = displayStaggerBuildUp;
	}

	@Override
	public int overhauleddamage$getLastStaggerBuildUp() {
		return this.lastStaggerBuildUp;
	}

	@Override
	public void overhauleddamage$setLastStaggerBuildUp(int lastStaggerBuildUp) {
		this.lastStaggerBuildUp = lastStaggerBuildUp;
	}

	@Override
	public long overhauleddamage$getLastStaggerBuildUpTime() {
		return this.lastStaggerBuildUpTime;
	}

	@Override
	public void overhauleddamage$setLastStaggerBuildUpTime(long lastStaggerBuildUpTime) {
		this.lastStaggerBuildUpTime = lastStaggerBuildUpTime;
	}

	@Override
	public long overhauleddamage$getStaggerBuildUpIconBlinkTime() {
		return this.staggerBuildUpBlinkTime;
	}

	@Override
	public void overhauleddamage$setStaggerBuildUpIconBlinkTime(long staggerBuildUpIconBlinkTime) {
		this.staggerBuildUpBlinkTime = staggerBuildUpIconBlinkTime;
	}

}
