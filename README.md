# ☁️ velocity module plugin

![Version](https://img.shields.io/badge/version-v1.0-blue)
![Java](https://img.shields.io/badge/java-23%2B-orange)
![Status](https://img.shields.io/badge/status-stable-brightgreen)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

Ein **modulares velocity module plugin**

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
