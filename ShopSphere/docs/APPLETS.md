# ShopSphere — Java Applets Conceptual Guide (`docs/APPLETS.md`)

This document provides the definitive academic examination guide to **Java Applets**, fulfilling the RTU Advanced Java syllabus topic **"Applets (Concept only)"**. It covers the applet lifecycle, architecture, reasons for obsolescence, and how the modern ShopSphere architecture replaces the concept.

---

## 1. What was a Java Applet?

A **Java Applet** was a small Java program designed to be transmitted over the internet from a web server and executed inside a client's web browser using a Java Virtual Machine (JVM) browser plugin.

Historically embedded in HTML using the `<applet>` tag:
```html
<applet code="com.shopsphere.docs.examples.AppletDemo.class" 
        archive="shopsphere-applet.jar" 
        width="450" 
        height="300">
    <param name="theme" value="dark"/>
    <p>Your browser does not support Java Applets.</p>
</applet>
```

---

## 2. The Applet Lifecycle (Crucial for RTU Viva)

An Applet did not have a `main()` method. Instead, its lifecycle was managed entirely by the web browser (or the JDK `appletviewer` tool) through five distinct methods inherited from `java.applet.Applet`:

```
               +---------------------------+
               | Browser Loads Web Page    |
               +---------------------------+
                             │
                             ▼
               +---------------------------+
               | 1. init()                 |  (Called ONCE: variable initialization, GUI layout)
               +---------------------------+
                             │
                             ▼
         ┌─────►+---------------------------+
         │      | 2. start()                |  (Called after init, or when revisiting page)
         │      +---------------------------+
         │                   │
Re-visit │                   ▼
page     │      +---------------------------+
         │      | 3. paint(Graphics g)      |  (Rendering graphics, text, shapes on canvas)
         │      +---------------------------+
         │                   │
         │                   ▼
         │      +---------------------------+
         │      | 4. stop()                 |  (User navigates away; suspends threads/animations)
         └──────┴───────────────────────────┘
                             │
                             ▼ (Browser closed / page unloaded)
               +---------------------------+
               | 5. destroy()              |  (Called ONCE: releases memory and native resources)
               +---------------------------+
```

### Detailed Lifecycle Method Descriptions:

1. **`init()`**:
   - Invoked **once** by the browser when the applet is first downloaded and loaded into memory.
   - Purpose: Read `<param>` values using `getParameter()`, initialize data structures, load images/fonts, and assemble AWT/Swing UI components.
2. **`start()`**:
   - Invoked immediately after `init()`, and **every time** the user re-enters or restores the browser tab.
   - Purpose: Starts animations, audio playback, or background polling threads.
3. **`paint(Graphics g)`**:
   - Inherited from `java.awt.Component`. Invoked whenever the applet's display area needs to be redrawn (e.g., when uncovered, resized, or when `repaint()` is called).
   - Parameter: `Graphics g` provides the drawing context (methods: `drawString()`, `drawRect()`, `setColor()`, `drawImage()`).
4. **`stop()`**:
   - Invoked automatically whenever the user navigates away from the web page containing the applet, minimizes the window, or switches tabs.
   - Purpose: Pauses CPU-intensive loops, stops audio, and suspends background threads to prevent wasting client CPU cycles.
5. **`destroy()`**:
   - Invoked **once** just before the browser unloads the applet from memory or closes.
   - Purpose: Releases persistent locks, closes sockets, and terminates threads.

---

## 3. The Applet Security "Sandbox"

Because applets were untrusted code downloaded over the public internet, they executed inside a strict security sandbox enforced by the browser's `SecurityManager`:
- **File System Restriction**: Untrusted applets could not read, write, or delete files on the client's local hard drive.
- **Network Restriction (Same-Origin Policy)**: An applet could only establish socket connections back to the originating host from which it was downloaded.
- **Native Code Restriction**: Applets could not execute native executables, load dynamic link libraries (`.dll` / `.so`), or invoke JNI.
- **System Properties**: Applets could not inspect sensitive system properties (e.g., user home directory, username).

---

## 4. Why Applets are Obsolete & Deprecated in Modern Java

| Factor | Description |
|---|---|
| **NPAPI Removal in Browsers** | All major web browsers eliminated Netscape Plugin API (NPAPI) support (Google Chrome in 2015, Mozilla Firefox in 2017, Apple Safari, Microsoft Edge). Modern browsers physically cannot run JVM plugins. |
| **Severe Security Vulnerabilities** | The browser-plugin interface was historically one of the most attacked zero-day attack surfaces on client operating systems. |
| **Official Java Deprecation** | Deprecated in **Java 9** ([JEP 289](https://openjdk.org/jeps/289)), marked for removal in **Java 17** ([JEP 398](https://openjdk.org/jeps/398)), and removed from standard modern JDKs. |
| **Rise of Web Standards** | HTML5, CSS3, JavaScript (ES6+), Canvas API, WebGL, WebSockets, and WebAssembly deliver superior graphical performance natively in the browser without any third-party plugins. |

---

## 5. How ShopSphere Replaces the Applet Concept

Rather than relying on obsolete browser plugins, ShopSphere adopts modern, industry-standard architectural alternatives:

1. **Rich Desktop Client Capability**:
   - Replaced by the **`swing-admin` module**: A standalone Java Swing application using MVC, `SwingWorker` for background concurrency, and runtime Pluggable Look and Feel.
2. **Browser Client Interactivity**:
   - Replaced by the **`web` module**: Standard Jakarta EE 10 web application hosted on Apache Tomcat 11, utilizing responsive HTML5, Bootstrap 5, CSS3, and JavaScript Fetch API.
3. **Reference Source Code**:
   - For academic evaluation, a clean demonstration class [`docs/examples/AppletDemo.java`](file:///d:/Advnace%20java/ShopSphere/docs/examples/AppletDemo.java) is provided in `docs/examples/` (isolated from the Maven build so modern JDK compilers never fail).

---

## 6. Key Viva Questions & Answers

1. **What is the difference between an Application and an Applet in Java?**
   - An **Application** has a `public static void main(String[] args)` method, runs as a standalone process under the direct control of the user, and has unrestricted access to local system resources (subject to OS permissions).
   - An **Applet** has no `main()` method, is launched and controlled by a browser/appletviewer lifecycle (`init, start, paint, stop, destroy`), and executes inside a restricted security sandbox.
2. **Can an Applet connect to any arbitrary database or IP address?**
   - No. By default, untrusted applets are bound by the **Same-Origin Policy** and can only establish socket/JDBC connections back to the host server from which the applet's `.class` or `.jar` file was downloaded.
3. **What is the difference between `Applet` and `JApplet`?**
   - `Applet` is an older AWT component (`java.applet.Applet`) that uses heavyweight native peer widgets. `JApplet` is a Swing component (`javax.swing.JApplet`) extending `Applet` that supports lightweight pluggable Swing components, double buffering, and root panes.
4. **Why is `AppletDemo.java` placed in `docs/examples/` rather than `src/main/java`?**
   - In Java 17 and Java 21, the `java.applet` package is deprecated for removal. Placing it in `docs/examples/` keeps the code available for conceptual viva inspection while preventing compiler errors during automated Maven multi-module builds.
