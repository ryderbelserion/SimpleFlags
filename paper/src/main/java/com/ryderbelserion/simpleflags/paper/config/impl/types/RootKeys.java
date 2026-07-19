package com.ryderbelserion.simpleflags.paper.config.impl.types;

import com.ryderbelserion.fusion.core.api.config.annotations.Comment;
import com.ryderbelserion.fusion.core.api.config.properties.builders.CommentsBuilder;
import com.ryderbelserion.fusion.core.api.config.properties.interfaces.IPropertyHolder;
import com.ryderbelserion.fusion.core.api.config.properties.objects.interfaces.Property;
import com.ryderbelserion.fusion.core.api.config.properties.objects.primitives.BooleanProperty;
import com.ryderbelserion.fusion.core.api.config.properties.objects.primitives.string.StringProperty;
import com.ryderbelserion.simpleflags.paper.api.enums.messages.State;
import org.jspecify.annotations.NonNull;
import java.util.List;
import static com.ryderbelserion.fusion.core.api.config.properties.PropertyBuilder.newBeanProperty;
import static com.ryderbelserion.fusion.core.api.config.properties.PropertyBuilder.newProperty;

public class RootKeys implements IPropertyHolder {

    @Override
    public void registerComments(@NonNull final CommentsBuilder configuration) {
        final List<String> deprecation = List.of(
                " ",
                "Warning: This section is subject to change so it is considered deprecated.",
                "This is your warning before the change happens. Please read the latest changelogs",
                " "
        );

        configuration.setComment(List.of(
                "Support: https://discord.gg/ktxCyUd7Ny",
                "Github: https://github.com/ryderbelserion",
                " ",
                "Issues: https://github.com/ryderbelserion/SimpleFlags/issues",
                "Features: https://github.com/ryderbelserion/SimpleFlags/issues",
                " ",
                "Sounds: https://mudkipdev.github.io/minecraft-sound-explorer/"
        ), "root");

        configuration.setComment(List.of(
                "Flags related to the Crafter block."
        ), "flags", "crafter-flag");

        configuration.setComment(List.of(
                "Configure the flags of the plugin."
        ), "flags");
    }

    @Comment("The prefix used in commands")
    public static final StringProperty command_prefix = newProperty(" <gradient:#e91e63:blue>CrazyCrates</gradient> | ", "root", "command_prefix");

    @Comment({
            "This option will tell the plugin to send all messages as action bars or messages in chat.",
            " ",
            "send_message -> sends messages in chat.",
            "send_actionbar -> sends messages in actionbar.",
            " "
    })
    public static final Property<State> message_state = newBeanProperty(State.class, State.send_message, "root", "message-state");

    @Comment("This flag prevent_drowning as you guessed it, it prevents drowning if enabled.")
    public static final BooleanProperty is_drowning_flag_enabled = newProperty(false, "flags", "drowning-flag");

    @Comment("This flag prevent_crafter_interaction prevents interacting with Crafters.")
    public static final BooleanProperty is_crafter_interaction_enabled = newProperty(false, "flags", "crafter-flag", "interaction");

    @Comment("This flag prevent_crafter_block prevents placing Crafters.")
    public static final BooleanProperty is_crafter_place_enabled = newProperty(false, "flags", "crafter-flag", "place");

    @Comment("This flag prevent_natural_spawning prevents natural mob spawns in regions.")
    public static final BooleanProperty is_natural_spawning_flag_enabled = newProperty(false, "flags", "natural-spawning-flag");
}