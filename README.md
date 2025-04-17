# JavaFX “Photoshop” Mini‑App

A lightweight **JavaFX** desktop application that showcases fundamental image‑processing operations (grayscale, blur, edge detection …) with a responsive GUI, progress bar and multithreaded pipeline.

---

## ✨ Key Features

| Function | Description |
|----------|-------------|
| **Load demo image** | Built‑in sample (Ingo photo) bundled in JAR; replace with your own via the *SOURCE_FILE* constant. |
| **Processing modes** | `GRAYSCALE`, `INVERT`, `GAUSSIAN_BLUR`, `SOBEL_EDGE`, `SEPIA`, `SHARPEN` *(extend in `ImageProcessor.java`)* |
| **Threaded execution** | Uses a single‑thread `ExecutorService`; heavy loops run off the JavaFX UI thread and update a `ProgressBar` via `Task` callbacks. |
| **Live preview** | Two `ImageView`s show *before* and *after* side‑by‑side once processing completes. |
| **Time measurement** | Elapsed duration printed and displayed under the progress bar. |
| **Export** | Result saved as PNG/JPG in `src/main/out/GeneratedImage_<timestamp>.{ext}`. |

---

## 🏗 Project Structure

```
PhotoshopApp/
├── src/main/java/com/.../photoshopapp/
│   ├── Main.java            # JavaFX launcher
│   ├── Controller.java      # GUI + actions
│   ├── ImageProcessor.java  # Dispatcher & IO
│   ├── ImageProcess.java    # Enum of process types
│   ├── ImagePixel.java      # Helper struct (r,g,b)
│   └── InputImage.java      # Utility for reading/ writing images
├── src/main/resources/
│   └── .../pexels-ingo.jpg  # Sample photo (replaceable)
├── mvnw + pom.xml           # Maven wrapper build
└── README.md
```

---

## 🚀 Quick Start

### 1 · Prerequisites
* **JDK 17** or later (JavaFX 21 compatible)
* Maven 3.9+ (wrapper provided)

### 2 · Build & Run
```bash
./mvnw clean javafx:run      # Linux/macOS
mvnw.cmd clean javafx:run     # Windows
```
Maven fetches JavaFX dependencies (via `org.openjfx` plugin) and launches the UI.

### 3 · Custom Image / Filter
1. Drop a new JPEG/PNG into `src/main/resources/...` and change `SOURCE_FILE` in `Controller.java`.
2. To add a new effect, implement a static method in `ImageProcessor` that takes `BufferedImage` → returns processed copy, then add it to the `ComboBox` list.

---

## ⚙️ Algorithm Notes
* **Grayscale** – average or luminosity method over RGB.
* **Gaussian Blur** – 3×3 kernel convolution.
* **Sobel Edge** – horizontal & vertical gradient magnitude.
* **Sharpen** – unsharp mask via Laplacian kernel.

All convolution kernels iterate pixel‑by‑pixel using `Raster` for speed. Progress is reported every row.

---

## 🧪 Testing
Simple CLI benchmark:
```bash
mvn -q exec:java -Dexec.mainClass="com.ahmetabdullahgultekin.photoshopapp.ImageProcessorBenchmark"
```
*(benchmark class not yet implemented – PRs welcome!)*

---

## 🛠 Roadmap
- Drag‑and‑drop image loader
- Zoom & pan with `ScrollPane`
- Batch processing folder → folder
- History undo stack
- Module‑info.java to make the JAR fully modular

---

## 🤝 Contributing
Fork, feature‑branch, and open a PR. Follow Google Java Style (checkstyle config in root). Screenshots/GIFs for UI changes are appreciated.

---

## 📜 License
Images under `resources/` belong to their original photographers.

> Crafted by **Ahmet Abdullah Gültekin** – enjoy hacking!

