package com.momentum.impl.modules.render.norender;

import com.momentum.api.event.FeatureListener;
import com.momentum.impl.events.vanilla.network.InboundPacketEvent;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketParticles;

/**
 * @author linus
 * @since 03/10/2023
 */
public class InboundPacketListener extends FeatureListener<NoRenderModule, InboundPacketEvent> {

    // common unicode crash message
    private final static String[] UNICODE_CRASH =
            "ā ȁ ́ Ё ԁ ܁ ࠁ ँ ਁ ଁ ก ༁ ခ ᄁ ሁ ጁ ᐁ ᔁ ᘁ ᜁ ᠁ ᤁ ᨁ ᬁ ᰁ ᴁ ḁ ἁ ℁ ∁ ⌁ ␁ ━ ✁ ⠁ ⤁ ⨁ ⬁ Ⰱ ⴁ ⸁ ⼁ 、  ㈁ ㌁ 㐁 㔁 㘁 㜁 㠁 㤁 㨁 㬁 㰁 㴁 㸁 㼁 䀁 䄁 䈁 䌁 䐁 䔁 䘁 䜁 䠁 䤁 䨁 䬁 䰁 䴁 丁 企 倁 儁 刁 匁 吁 唁 嘁 圁 堁 夁 威 嬁 封 崁 币 弁 态 愁 戁 持 搁 攁 昁 朁 栁 椁 樁 欁 氁 洁 渁 漁 瀁 焁 爁 猁 琁 甁 瘁 省 码 礁 稁 笁 簁 紁 縁 缁 老 脁 舁 茁 萁 蔁 蘁 蜁 蠁 褁 訁 謁 谁 贁 踁 輁 送 鄁 鈁 錁 鐁 锁 阁 霁 頁 餁 騁 鬁 鰁 鴁 鸁 鼁 ꀁ ꄁ ꈁ ꌁ ꐁ ꔁ ꘁ ꜁ ꠁ ꤁ ꨁ ꬁ 각 괁 긁 꼁 뀁 넁 눁 댁 됁 딁 똁 뜁 렁 뤁 먁 묁 밁 봁"
                    .split(" ");

    /**
     * Default constructor
     *
     * @param feature The associated feature
     */
    protected InboundPacketListener(NoRenderModule feature) {
        super(feature);
    }

    @Override
    public void invoke(InboundPacketEvent event) {

        // packet for particle spawns
        if (event.getPacket() instanceof SPacketParticles) {

            // particle lag fix
            if (feature.particleLagOption.getVal()) {

                // packet from event
                SPacketParticles packet = (SPacketParticles) event.getPacket();

                // large particle count
                // fix for 254n_m's crash plugin
                if (packet.getParticleCount() > 100) {

                    // cancels particles from rendering
                    event.setCanceled(true);
                }
            }
        }

        // packet for server chat messages
        else if (event.getPacket() instanceof SPacketChat) {

            // unicode lag fix
            if (feature.unicodeLagOption.getVal()) {

                // packet from event
                SPacketChat packet = (SPacketChat) event.getPacket();

                // recieved message
                String message = packet.getChatComponent().getUnformattedText();

                // check each unicode crash char
                for (int i = 0; i < UNICODE_CRASH.length; i++) {

                    // check if the message contains the crash char
                    if (message.contains(UNICODE_CRASH[i])) {

                        // prevent message from printing in char
                        event.setCanceled(true);
                        break;
                    }
                }
            }
        }
    }
}
