/*
 ** 2014 Juli 28
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.minema.client.modules;

import cpw.mods.fml.relauncher.ReflectionHelper;
import info.ata4.minecraft.minema.client.config.MinemaConfig;
import info.ata4.minecraft.minema.util.PrivateFields;
import info.ata4.minecraft.minema.util.PrivateMethods;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class DisplaySizeModifier extends CaptureModule {
    
    private static final Logger L = LogManager.getLogger();
    private static final Minecraft MC = Minecraft.getMinecraft();
    
    public DisplaySizeModifier(MinemaConfig cfg) {
        super(cfg);
    }

    @Override
    protected void doEnable() {
        resize(cfg.getFrameWidth(), cfg.getFrameHeight());
        setFramebufferTextureSize(Display.getWidth(), Display.getHeight());
    }

    @Override
    protected void doDisable() {
        resize(Display.getWidth(), Display.getHeight());
    }
    
    public void resize(int width, int height) {
        try {
            Method resize = ReflectionHelper.findMethod(Minecraft.class, MC, PrivateMethods.MINECRAFT_RESIZE, Integer.TYPE, Integer.TYPE);
            resize.invoke(MC, width, height);
        } catch (Exception ex) {
            throw new RuntimeException("Can't resize display", ex);
        }
    }
    
    public void setFramebufferTextureSize(int width, int height) {
        try {
            Framebuffer fb = MC.getFramebuffer();
            ReflectionHelper.setPrivateValue(Framebuffer.class, fb, width, PrivateFields.FRAMEBUFFER_FRAMEBUFFERTEXTUREWIDTH);
            ReflectionHelper.setPrivateValue(Framebuffer.class, fb, height, PrivateFields.FRAMEBUFFER_FRAMEBUFFERTEXTUREHEIGHT);
        } catch (Exception ex) {
            throw new RuntimeException("Can't set framebuffer texture size", ex);
        }
    }
}