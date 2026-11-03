# Depression Mod
### [McMod](https://www.mcmod.cn/class/15427.html) | [Modrinth](https://modrinth.com/mod/depression) | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/depression)

This branch targets **Minecraft 1.21.1** on **Fabric** and **NeoForge** (Java 21).
Install Architectury API **13.0.8 or newer** for your loader. Fabric also needs Fabric API.
The Forge 1.20.1 version remains on the `1.20.1` branch.

Build both loader JARs with `./gradlew build` (or `gradlew.bat build` on Windows).
The installable files are `fabric/build/libs/depression-fabric-0.2.3+1.21.1.jar`
and `neoforge/build/libs/depression-neoforge-0.2.3+1.21.1.jar`.
Use the JAR matching your loader; files with `dev`, `shadow`, or `sources` classifiers are development artifacts.

In this mod, we add two attributes to players: **emotion** and **mental health**. You can see your emotion through the emoji displayed on your XP bar. When you get hurt, your emotion will drop and you may develop PTSD related to the damage source (PTSD will make you take more emotional damage from the same source).

**Emotion** determines how your mental health changes (either up or down) and how quickly it changes. When your mental health is low, you may develop **Depression** (which has three levels depending on your mental health) and experience symptoms such as insomnia. 

You can gauge your mental health by writing a diary at night. (The diary can be crafted using a book, a quill, and a flower.) If you develop severe depression, you may need to look for a psychologist, who will spawn in the psychological clinic in the village.
