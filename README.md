# ☁️ velocity module plugin

![Version](https://img.shields.io/badge/version-v1.2.0-blue)
![Java](https://img.shields.io/badge/java-21%2B-orange)
![Status](https://img.shields.io/badge/status-stable-brightgreen)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

Ein **modulares, verteiltes Cloud-System** zur Orchestrierung von  
**Minecraft Servern (Paper / Spigot / Velocity)** über mehrere Nodes –  
entwickelt für **Stabilität, Skalierbarkeit und Performance**.

---

## 🚀 Features

- 🔗 **Peer-to-Peer Node Netzwerk** (kein zentraler Master nötig)
- ⚡ **Netty-basierte Hochleistungs-Kommunikation**
- 🧩 **Modulares Multi-Project Setup**
- 🛡 **Authentifizierte & sichere Node-Verbindungen**
- 🔄 **Automatisches Node- & Service-Failover**
- 🎮 **Plugin-API für Server & Proxies**
- 📡 **Heartbeat & Cluster-State Synchronisation**

---

## 📦 Projektstruktur

```text
cloud/
├── cloud-api          # Öffentliche API für Plugins & Tools
├── cloud-protocol     # Packet-, Serializer- & Registry-System
├── cloud-node         # Standalone Node Application
├── cloud-plugin       # Minecraft Plugin (Paper / Velocity)
└── cloud-web-api      # (optional) REST / Web API
