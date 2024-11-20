package io.github.hangga.delvelin.detectors;

import io.github.hangga.delvelin.properties.Vulnerabilities;

public class NonThreadSafeDetector extends BaseDetector {

    private static class NonThreadSafe {

        final String instanceName;
        final String firstAlternative;
        final String secondAlternative;

        public String getInstanceName() {
            return instanceName;
        }

        public String getFirstAlternative() {
            return firstAlternative;
        }

        public String getSecondAlternative() {
            return secondAlternative;
        }

        public String getThirdAlternative() {
            return thirdAlternative;
        }

        public NonThreadSafe(String instanceName, String firstAlternative, String secondAlternative, String thirdAlternative) {
            this.instanceName = instanceName;
            this.firstAlternative = firstAlternative;
            this.secondAlternative = secondAlternative;
            this.thirdAlternative = thirdAlternative;
        }

        final String thirdAlternative;
    }

    private final NonThreadSafe[] safeArray;

    {
        safeArray = new NonThreadSafe[] { new NonThreadSafe("ArrayList", "CopyOnWriteArrayList", "Collections.synchronizedList(new ArrayList<>())", null),
            new NonThreadSafe("LinkedList", "ConcurrentLinkedDeque", "Collections.synchronizedList(new LinkedList<>())", null),
            new NonThreadSafe("HashMap", "ConcurrentHashMap", "Collections.synchronizedMap(new HashMap<>())", null),
            new NonThreadSafe("TreeMap", "ConcurrentSkipListMap", "Collections.synchronizedSortedMap(new TreeMap<>())", null),
            new NonThreadSafe("HashSet", "CopyOnWriteArraySet", "Collections.synchronizedSet(new HashSet<>())", null),
            new NonThreadSafe("TreeSet", "ConcurrentSkipListSet", "Collections.synchronizedSortedSet(new TreeSet<>())", null),
            new NonThreadSafe("PriorityQueue", "PriorityBlockingQueue", null, null),
            new NonThreadSafe("ArrayDeque", "ConcurrentLinkedDeque", "LinkedBlockingDeque", null),
            new NonThreadSafe("StringBuilder", "StringBuffer", null, null), new NonThreadSafe("BitSet", "AtomicIntegerArray", "AtomicLongArray", null),
            new NonThreadSafe("LinkedHashMap", "Collections.synchronizedMap(new LinkedHashMap<>())", null, null),
            new NonThreadSafe("WeakHashMap", "Collections.synchronizedMap(new WeakHashMap<>())", null, null),
            new NonThreadSafe("EnumMap", "Collections.synchronizedMap(new EnumMap<>(Enum.class))", null, null),
            new NonThreadSafe("MutableList", "Collections.synchronizedList(mutableListOf())", null, null),
            new NonThreadSafe("MutableSet", "Collections.synchronizedSet(mutableSetOf())", null, null),
            new NonThreadSafe("MutableMap", "Collections.synchronizedMap(mutableMapOf())", null, null),
            new NonThreadSafe("Deque", "ConcurrentLinkedDeque", "LinkedBlockingDeque", null),
            new NonThreadSafe("Queue", "ConcurrentLinkedQueue", "LinkedBlockingQueue", null),
            new NonThreadSafe("MutableCollection", "Collections.synchronizedCollection(mutableListOf())", null, null),
            new NonThreadSafe("Counter", "AtomicInteger", "ReentrantLock", "synchronized") };
    }

    public NonThreadSafeDetector() {
        this.vulnerabilities = Vulnerabilities.NON_THREAD_SAFE_DATA_STRUCTURE;
    }

    @Override
    public void detect(String line, int lineNumber) {

    }

    @Override
    public void detect(String content) {
        if (!isValidExtension()) {
            return;
        }

        String[] lines = content.split("\\r?\\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            for (NonThreadSafe safeStructure : safeArray) {
                if (line.contains(safeStructure.getInstanceName())) {
                    boolean isAlternativeUsed = (safeStructure.getFirstAlternative() != null && line.contains(safeStructure.getFirstAlternative())) ||
                        (safeStructure.getSecondAlternative() != null && line.contains(safeStructure.getSecondAlternative())) ||
                        (safeStructure.getThirdAlternative() != null && line.contains(safeStructure.getThirdAlternative()));

                    if (!isAlternativeUsed) {
                        StringBuilder msg = new StringBuilder("Consider using ");
                        if (safeStructure.getFirstAlternative() != null) {
                            msg.append("<code>")
                                .append(safeStructure.getFirstAlternative())
                                .append("</code>");
                        }
                        if (safeStructure.getSecondAlternative() != null) {
                            msg.append(" or ")
                                .append("<code>")
                                .append(safeStructure.getSecondAlternative())
                                .append("</code>");
                        }
                        if (safeStructure.getThirdAlternative() != null) {
                            msg.append(" or ")
                                .append("<code>")
                                .append(safeStructure.getThirdAlternative())
                                .append("</code>");
                        }

                        setValidVulnerability(specificLocation(i + 1), safeStructure.getInstanceName(), msg.toString());
                    }
                }
            }
        }
    }

    private boolean isValidExtension() {
        return extName.equals(".kt") || extName.equals(".java");
    }
}
