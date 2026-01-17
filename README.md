# ☁️ velocity module plugin

![Version](https://img.shields.io/badge/version-v1.2.0-blue)
![Java](https://img.shields.io/badge/java-21%2B-orange)
![Status](https://img.shields.io/badge/status-stable-brightgreen)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

Ein **modulares velocity module plugin** zur Orchestrierung von  
**Minecraft Servern (Paper / Spigot / Velocity)** über mehrere Nodes –  
entwickelt für **Stabilität, Skalierbarkeit und Performance**.

---

## 🚀 Features

- 🔗 **Live Reloadable** velocity Module Plugin 

---

## 📦 Example Main Class

```java
@Module(id = "motd-module", name = "MOTD Module", version = "1.0-SNAPSHOT")
public class MotdModule implements VelocityModule {

    private ModuleContext context;

    @Override
    public void onEnable(ModuleContext ctx) {
        this.context = ctx;

        this.context.moduleManager().registerListener(this);
    }

    @Override
    public void onDisable() {

    }

}
