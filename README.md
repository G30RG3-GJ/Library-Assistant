# Material Design Library Management Software using JavaFX

This is a library management software developed using JavaFX. The codebase has been fully upgraded to **v1.2.0 (Rebuilded GSTUDIO)** with modern aesthetics, advanced visual effects, and full support for modern OpenJDK runtimes.

---

### 🚀 New Features (v1.2.0 - Rebuilded GSTUDIO)

* **✨ Parallax Style Login Screen**: An interactive, responsive 2D mouse-hover parallax effect in the login window. Moving your cursor dynamically shifts floating, blurred neon background spheres (`javafx.scene.layout.AnchorPane`) at different depth-perceived speeds.
* **🔮 Glassmorphism Aesthetic Overhaul**: Translucent frosted panels, modern slate-dark palette (`#0d1117`), amber highlights (`#f5b041`), custom shadows, and premium layout adjustments in `dark-theme.css`.
* **🛠️ OpenJDK 17 & JavaFX 17 Compatibility**: Integrated OpenJFX 17 libraries directly inside `libs/` and added a lightweight `MainLauncher.java` entry wrapper. This solves classpath restrictions and lets you run the app seamlessly on modern JDK 17 environments.
* **📱 Attribution**: Added **"Rebuilded GSTUDIO"** to the "About" screen layout.
* **📦 Fully Portable Scripts**: Upgraded `run_app.ps1` and `package_app.ps1` to use `$PSScriptRoot` instead of hardcoded directories (`d:\lib...`). The packaging script also automatically handles JAR Class-Path manifest line-wrapping (strictly adhering to the 72-byte limit).
* **🧪 PowerShell Test Runner**: Added `run_tests.ps1` for easy native compilation and JUnit testing on Windows environments.

---

### 🚀 ქართული აღწერა (v1.2.0 სიახლეები)

* **✨ Parallax სტილის შესვლის ფანჯარა**: რეალურ დროში მაუსის მოძრაობაზე რეაგირებადი 2D parallax ეფექტი. მაუსის გადაადგილებისას, ფონური მანათობელი და დაბინდული ნეონის სფეროები მოძრაობენ სხვადასხვა სიჩქარით, რაც ქმნის სიღრმის გასაოცარ ვიზუალურ ეფექტს.
* **🔮 Glassmorphism დიზაინი**: ნახევრად გამჭვირვალე ყინულისებრი ბარათის პანელი (Frosted Glass Card), თანამედროვე მუქი ფერთა გამა (Slate Dark) და პრემიუმ კლასის ნეონის/ქარვისფერი აქცენტები `dark-theme.css`-ში.
* **🛠️ OpenJDK 17 + OpenJFX 17 თავსებადობა**: JavaFX-ის ბიბლიოთეკები პირდაპირ ჩაშენებულია პროექტის `libs/` საქაღალდეში. დამატებულია `MainLauncher.java` კლასი, რაც შესაძლებელს ხდის პროექტი დაკომპილირდეს და გაეშვას ნებისმიერ თანამედროვე კომპიუტერზე დამატებითი გარემოს კონფიგურაციის გარეშე.
* **📱 GSTUDIO ატრიბუცია**: "About" (აღწერის) გვერდზე წარმატებით ჩაემატა ტექსტი **"Rebuilded GSTUDIO"**.
* **📦 პორტატული Build სკრიპტები**: `run_app.ps1` და `package_app.ps1` გასწორდა და გახდა სრულად პორტატული `$PSScriptRoot`-ით. სკრიპტი ავტომატურად ახდენს JAR Class-Path-ის ოპტიმალურ ფორმატირებას 72-ბაიტიანი ლიმიტის გათვალისწინებით.
* **🧪 ტესტირების გარემო**: შეიქმნა `run_tests.ps1` სკრიპტი, რომელიც უზრუნველყოფს JUnit ტესტების მარტივ და ავტომატიზებულ გაშვებას.

---

### 💻 Getting Started

#### Prerequisites
* **Java 17 (JDK 17)** or Oracle JDK 8 (everything is fully compatible).
* NetBeans, IntelliJ IDEA, or command-line PowerShell.

#### Running the Application
To run the application locally from the terminal:
```powershell
powershell -ExecutionPolicy Bypass -File .\run_app.ps1
```

#### Running Tests
To compile and execute unit tests natively:
```powershell
powershell -ExecutionPolicy Bypass -File .\run_tests.ps1
```

#### Building from Source (Release Package)
To create a production-ready release package (`LibraryAssistant-v1.2.0.zip` containing JAR, manifest, and dependencies):
```powershell
powershell -ExecutionPolicy Bypass -File .\package_app.ps1
```
The output zip file will be generated in the `dist` folder.

---

### 🔑 Default Login Credentials
| Username  | Password |
| ------------- | ------------- |
| admin  | admin  |

### 📚 Libraries Used
* [JFoenix](https://github.com/jfoenixadmin/JFoenix) - JavaFX Material Design Library
* [OpenJFX 17](https://openjfx.io/) - JavaFX SDK for modern JDK support
* [Apache Derby](https://db.apache.org/derby/) - Standalone Relational database
* [Apache Commons](https://commons.apache.org/) - For creating SHA hash and CSV Export
* [GSon](https://github.com/google/gson) - JSON Library. Used for storing configuration
* [FontawesomeFX](https://bitbucket.org/Jerady/fontawesomefx) - Icon library
* [Apache PDFBox](https://pdfbox.apache.org/) - PDF Export
* [JavaMail API](http://www.oracle.com/technetwork/java/javamail/index.html) - Email Notification
