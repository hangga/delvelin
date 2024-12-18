<img src="https://github.com/hangga/delvelin/blob/main/doc/delvelin-soft-black.webp?raw=true" 
alt="Delvelin Scan Demo" width="260">

![Java](https://img.shields.io/badge/Java-8+-blue?logo=java) ![Kotlin](https://img.shields.io/badge/Kotlin-1.5+-blueviolet?logo=kotlin) ![Gradle Plugin](https://img.shields.io/badge/Gradle-Plugin-brightgreen?logo=gradle) ![CWE](https://img.shields.io/badge/CWE-Standards-orange) ![CVSS](https://img.shields.io/badge/CVSS-Severity-red)
[![OSV.dev](https://img.shields.io/badge/OSV.dev-Vulnerability%20Database-blue)](https://google.github.io/osv.dev/)
[![License](https://img.shields.io/badge/license-MIT-green)](LICENSE) 

**Delveline** is a Code Vulnerability Analyzer for Java and Kotlin that supports best practices in security and risk management.  
By aligning with ISO/IEC 27001 principles, Delveline helps raise security awareness and improve software development security.


![Delvelin Scan Demo](https://delvelin.github.io/assets/img/delvelin-scan-new.gif)

## **How it Works**
We leverage:
- [CWE (Common Weakness Enumeration)](https://cwe.mitre.org/data/slices/699.html): A global standard for identifying and categorizing vulnerabilities.
- [CVSS (Common Vulnerability Scoring System)](https://www.first.org/cvss/calculator/3.0): A framework for scoring the severity of vulnerabilities.
- [OSV (Open Source Vulnerabilities)](https://google.github.io/osv.dev/): A comprehensive database for open-source vulnerability information.
- **ISO/IEC 27001 Alignment**: Supporting security awareness and risk management practices aligned with global information security standards.

## **Sample Output**

<div style="display: flex; justify-content: space-between; align-items: center; 
background-color: #0782FF; padding: 10px;">
    <img style="width: 48%;"
         src="https://github.com/delvelin/blog/blob/master/_posts/delvelin-report-console-1.png?raw=true" 
         alt="Delveline Report Console View" />
    <img style="width: 48%;"
         src="https://github.com/delvelin/blog/blob/master/_posts/delvelin-report-html.png?raw=true" 
         alt="Delveline Report HTML View" />
</div>

[//]: # (### **Console Log**)

[//]: # (![Delvelin Scan Console]&#40;https://github.com/delvelin/blog/blob/master/_posts/delvelin-report-console-1.png?raw=true&#41;)

[//]: # ()
[//]: # (### **HTML Format**)

[//]: # ()
[//]: # (![Delvelin Scan Demo]&#40;https://github.com/delvelin/blog/blob/master/_posts/delvelin-report-html.png?raw=true&#41;)

For a detailed report, output can be saved in HTML format:
[Example HTML Output](https://delvelin.github.io/docs/vulnerability-report.html)

[//]: # ()
[//]: # (## Key Features)

[//]: # ()
[//]: # (- **ISO/IEC 27001 Alignment**: Delveline supports best practices in security awareness and risk management.)

[//]: # (- **CWE and CVSS Integration**: Identify and prioritize vulnerabilities using industry standards.)

[//]: # (- **Dependency Scanning with OSV.dev**: Detect known CVEs in libraries and dependencies.)

[//]: # (- **Thread-Safety Detection**: Highlight unsafe structures in multi-threading scenarios.)

[//]: # ()
[//]: # (## Advantages)

[//]: # ()
[//]: # (### **1. Security-Oriented Focus**)

[//]: # (- **Delveline** excels as a security analysis tool, offering the ability to detect vulnerabilities such as:)

[//]: # (    - Non-thread-safe data structures &#40;e.g., `HashMap`, `ArrayList`&#41; in multi-threading scenarios.)

[//]: # (    - Hardcoded sensitive data like API tokens, passwords, or private keys.)

[//]: # (    - XSS vulnerabilities through regex pattern analysis on code strings.)

[//]: # (    - Detection aligned with **OWASP ASVS**, **CWE**, and **OSV.dev** standards.)

[//]: # ()
[//]: # (  **OSV.dev**, backed by Google, provides an extensive database for detecting known vulnerabilities &#40;CVEs&#41; in dependencies and libraries used in your project. This enables **Delveline** to identify outdated or vulnerable dependencies more effectively.)

[//]: # ()
[//]: # (### **2. Industry Standards and Vulnerability Scoring**)

[//]: # (- **Delveline** integrates **CWE &#40;Common Weakness Enumeration&#41;** as a reference for defining vulnerabilities.)

[//]: # (- It also uses **CVSS &#40;Common Vulnerability Scoring System&#41;** for severity scoring and prioritization of fixes.)

[//]: # (- By incorporating **OSV.dev**, it adds another layer of detection by identifying known CVEs in project dependencies.)


[//]: # (### **4. Multi-Platform Execution Support**)

[//]: # (- **Delveline** can be executed in various ways:)

[//]: # (    - As a standalone Java library.)

[//]: # (    - Through a **Gradle Plugin**, enabling seamless integration into build pipelines.)

[//]: # (    - As an **IntelliJ IDEA Plugin**, providing direct IDE integration without additional configuration.)

[//]: # (---)

[//]: # (### **3. Runtime-Assisted Static Analysis**)

[//]: # (- **Delveline** employs a unique **runtime-assisted static analysis** approach, allowing static analysis to be supplemented by runtime data, making it more adaptive than purely static tools.)

[//]: # ()
[//]: # ()
[//]: # (### **4. Target Audience**)

[//]: # (- **Delveline** is designed for projects requiring deep security analysis.)

[//]: # (---)

[//]: # (**Conclusion:**  )

[//]: # (**Delveline** stands out if your project needs:)

[//]: # (- Comprehensive security analysis based on industry standards.)

[//]: # (- Identification of vulnerable dependencies through **OSV.dev** and CVE detection.)

[//]: # (- Detection of thread-safety and runtime issues.)

[//]: # (- Flexible integrations &#40;Gradle, IntelliJ, Kotlin DSL&#41;.)

> **Disclaimer**: Delveline may not identify all vulnerabilities but serves as a powerful first step in securing your codebase.

## **How to Use**
- <a href="https://github.com/hangga/delvelin/blob/main/using-gradle-plugin.md">Using Delvelin 
  Gradle
  Plugin</a>
- <a href="https://github.com/hangga/delvelin/blob/main/using-library.md">Using Delvelin Library</a>

---

## **License**
This project is licensed under [MIT License](LICENSE).

---

## **Contributing**
Contributions are welcome! Feel free to submit issues or pull requests for new features or improvements.

This project is still a work in progress, and your contributions are highly valuable in helping us improve and refine it.

If you find this project useful and would like to support its development, we would greatly appreciate your donations. Your generosity will go a long way in ensuring the growth and sustainability of this initiative.

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/F1F215NPV4)

[![Support via PayPal](https://cdn.rawgit.com/twolfson/paypal-github-button/1.0.0/dist/button.svg)](https://www.paypal.me/hanggaajisayekti/)

Thank you for your support!

---

### **Connect**
Feel free to reach out for questions, suggestions, or contributions:
- 📧 Email: bazeniancode@gmail.com
- 🌐 [GitHub Repository](https://github.com/hangga/delvelin)
