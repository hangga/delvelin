**Delveline Code Vulnerability Analysis Workflow**

1. **Input Source Code**
   
    - **Using Delvelin Plugin** : The user installs the Delveline plugin or library into the 
      project using Gradle or 
   Maven. If the user chooses to use the Delveline plugin, it will run in Gradle with a task 
   called `delvelinScan`. If the user opts for the library, they must run Delveline from a unit test.

   - **Using delvelin Library** : The user can also run Delveline in a GitLab CI pipeline.

2. **Delveline Analysis with Two Approaches**
   2.1. **Static Analysis for Allowed File Types**  
   (By default: .java, .kt, .gradle, .kts, and .xml)  
   The following vulnerabilities are checked:
    1. **Multi-thread Safety Check**
        - Analyzes to detect the use of non-thread-safe data structures such as `HashMap`, `ArrayList`, etc., in multi-threading scenarios.

    2. **Sensitive Data Detection**
        - Delveline scans the source code to detect hardcoded sensitive data, such as:
            - API Tokens
            - Passwords
            - Private Keys

    3. **XSS Vulnerability Scan**
        - Analyzes XSS patterns using regex on the code strings to detect potential vulnerabilities.

    4. **CWE Reference Integration**
        - The analysis results are linked to the vulnerability list from CWE (Common Weakness Enumeration) to provide the appropriate vulnerability categories.

    5. **CVSS Scoring**
        - Delveline uses the CVSS (Common Vulnerability Scoring System) to score the severity and prioritize fixing vulnerabilities.

    6. **Dependency Vulnerability Detection (OSV.dev)**
        - Delveline scans the project for outdated or vulnerable dependencies with the help of the OSV.dev database.

    7. **Generate Analysis Report**
        - Delveline generates a final report that includes:
            - Details of the vulnerabilities found
            - Severity score based on CVSS
            - Recommendations for remediation

   2.2. **Analyze Libraries/Dependencies Used in the Project**  
   Delveline checks the .gradle, .kts, or .xml files for any listed dependencies that have known vulnerabilities in OSV.dev.

3. **Results Display**  
   After completing the two analysis approaches, the results will be displayed in the form of a LOG, JSON, or HTML, depending on the user's configuration.  
   **Note:** For GitLab CI, only LOG output is available.