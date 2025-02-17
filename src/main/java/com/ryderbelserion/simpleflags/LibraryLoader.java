package com.ryderbelserion.simpleflags;

import com.ryderbelserion.simpleflags.enums.Plugins;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class LibraryLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addRepository(new RemoteRepository.Builder("paper", "default", "https://repo.papermc.io/repository/maven-public").build());

        resolver.addRepository(new RemoteRepository.Builder("crazycrewReleases", "default", "https://repo.crazycrew.us/releases/").build());

        resolver.addDependency(Plugins.configme.asDependency());
        resolver.addDependency(Plugins.fusion.asDependency());

        classpathBuilder.addLibrary(resolver);
    }
}