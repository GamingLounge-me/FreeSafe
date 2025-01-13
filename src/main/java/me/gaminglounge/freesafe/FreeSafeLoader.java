package me.gaminglounge.freesafe;

import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;

public class FreeSafeLoader implements PluginLoader {
        @Override
    public void classloader(PluginClasspathBuilder classpathBuilder){
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        resolver.addRepository(new RemoteRepository.Builder(
            "MavenCentralLoad",
            "default",
            "https://repo1.maven.org/maven2/"
            ).build());
        resolver.addRepository(new RemoteRepository.Builder(
            "EngineHub",
            "default",
            "https://maven.enginehub.org/repo/"
            ).build());
        resolver.addDependency(new Dependency(
            new DefaultArtifact("dev.jorel:commandapi-bukkit-shade-mojang-mapped:9.7.0"),
            null));
        resolver.addDependency(new Dependency(
            new DefaultArtifact("com.sk89q.worldguard:worldguard-bukkit:7.0.13-SNAPSHOT"),
            null));
        resolver.addDependency(new Dependency(
            new DefaultArtifact("com.sk89q.worldedit:worldedit-bukkit:7.3.10-SNAPSHOT"),
            null));
        classpathBuilder.addLibrary(resolver);
    }
}
