package com.bot.insched.discord.embed;

import net.dv8tion.jda.api.EmbedBuilder;

public class InschedEmbed extends EmbedBuilder{
    public InschedEmbed() {
        super();
        super.setColor(0xf4562);
        super.setFooter("Created by InSched | Your instant scheduler!");
    }
}
