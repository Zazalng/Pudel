package mimikko.zazalng.puddle.handlers.database.collection;

import org.apache.commons.collections4.ListUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import mimikko.zazalng.puddle.PuddleWorld;
import mimikko.zazalng.puddle.contracts.database.collection.CollectionEach;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Collection implements Cloneable, Iterable<DataRow> {
    public static final Collection EMPTY_COLLECTION = new Collection(
            new HashMap<>(),
            ListUtils.unmodifiableList(new ArrayList<>())
    );
    private final HashMap<String, String> keys;
    private final List<DataRow> items;
    
    public Collection() {
        this.keys = new HashMap<>();
        this.items = new ArrayList<>();
    }
    
    private Collection(HashMap<String, String> keys, List<DataRow> items) {
        this.keys = keys;
        this.items = items;
    }
    
    public Collection(@Nonnull Collection instance) {
        this.keys = new HashMap<>();
        this.items = new ArrayList<>();

        for (DataRow row : instance.all()) {
            items.add(new DataRow(row));
        }
    }
    
    public Collection(@Nonnull List<Map<String, Object>> items) {
        this.keys = new HashMap<>();
        this.items = new ArrayList<>();

        for (Map<String, Object> row : items) {
            row.keySet().stream().filter((key) -> (!keys.containsKey(key))).forEach((key) -> {
                keys.put(key, row.get(key).getClass().getTypeName());
            });

            this.items.add(new DataRow(row));
        }
    }
    
    public Collection(@Nullable ResultSet result) throws SQLException {
        this.keys = new HashMap<>();
        this.items = new ArrayList<>();

        if (result == null) {
            return;
        }

        ResultSetMetaData meta = result.getMetaData();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            keys.put(meta.getColumnLabel(i), meta.getColumnClassName(i));
        }

        while (result.next()) {
            Map<String, Object> array = new HashMap<>();

            for (String key : keys.keySet()) {
                array.put(key, result.getString(key));
            }

            items.add(new DataRow(array));
        }

        if (!result.isClosed()) {
            result.close();
        }
    }
    
    public List<DataRow> all() {
        return items;
    }
    
    public double avg() {
        return avg(keys.keySet().iterator().next());
    }
    
    public double avg(String field) {
        if (isEmpty() || !keys.containsKey(field)) {
            return 0;
        }

        BigDecimal decimal = new BigDecimal(0);

        for (DataRow row : items) {
            Object obj = row.get(field);

            switch (obj.getClass().getTypeName()) {
                case "java.lang.Double":
                    decimal = decimal.add(new BigDecimal((Double) obj));
                    break;

                case "java.lang.Long":
                    decimal = decimal.add(new BigDecimal((Long) obj));
                    break;

                case "java.lang.Integer":
                    decimal = decimal.add(new BigDecimal((Integer) obj));
                    break;

                case "java.lang.Float":
                    decimal = decimal.add(new BigDecimal((Float) obj));
                    break;
            }
        }

        return decimal.divide(new BigDecimal(items.size())).doubleValue();
    }
    
    public List<Collection> chunk(int size) {
        List<Collection> chunk = new ArrayList<>();

        int index = 0, counter = 0;
        for (DataRow row : items) {
            if (counter++ >= size) {
                index++;
                counter = 0;
            }

            try {
                Collection get = chunk.get(index);

                get.add(row);
            } catch (IndexOutOfBoundsException e) {
                Collection collection = new Collection();

                collection.add(row);

                chunk.add(index, collection);
            }
        }

        return chunk;
    }
    
    public boolean contains(Object item) {
        return items.stream().anyMatch((row)
                -> (row.keySet().stream().anyMatch((key)
                        -> (row.get(key).equals(item)))));
    }
    
    public Collection copy() {
        return new Collection(this);
    }
    
    public Collection each(CollectionEach comparator) {
        ListIterator<DataRow> iterator = items.listIterator();

        while (iterator.hasNext()) {
            comparator.forEach(iterator.nextIndex(), iterator.next());
        }

        return this;
    }
    
    public DataRow first() {
        if (items.isEmpty()) {
            return null;
        }

        return items.get(0);
    }
    
    public DataRow get(int index) {
        return items.get(index);
    }
    
    public HashMap<String, String> getKeys() {
        return keys;
    }
    
    public List<DataRow> getItems() {
        return items;
    }
    
    public boolean has(String field) {
        return keys.containsKey(field);
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public DataRow last() {
        if (isEmpty()) {
            return null;
        }

        return items.get(items.size() - 1);
    }
    
    public int maxInt(String field) {
        if (!has(field)) {
            return Integer.MIN_VALUE;
        }

        int max = Integer.MIN_VALUE;
        for (DataRow row : items) {
            int x = row.getInt(field);

            if (max < x) {
                max = x;
            }
        }

        return max;
    }
    
    public long maxLong(String field) {
        if (!has(field)) {
            return Long.MIN_VALUE;
        }

        long max = Long.MIN_VALUE;
        for (DataRow row : items) {
            long x = row.getLong(field);

            if (max < x) {
                max = x;
            }
        }

        return max;
    }
    
    public double maxDouble(String field) {
        if (!has(field)) {
            return Double.MIN_VALUE;
        }

        double max = Double.MIN_VALUE;
        for (DataRow row : items) {
            double x = row.getDouble(field);

            if (max < x) {
                max = x;
            }
        }

        return max;
    }
    
    public float maxFloat(String field) {
        if (!has(field)) {
            return Float.MIN_VALUE;
        }

        float max = Float.MIN_VALUE;
        for (DataRow row : items) {
            float x = row.getFloat(field);

            if (max < x) {
                max = x;
            }
        }

        return max;
    }
    
    public int minInt(String field) {
        if (!has(field)) {
            return Integer.MAX_VALUE;
        }

        int min = Integer.MAX_VALUE;
        for (DataRow row : items) {
            int x = row.getInt(field);

            if (min > x) {
                min = x;
            }
        }

        return min;
    }
    
    public long minLong(String field) {
        if (!has(field)) {
            return Long.MAX_VALUE;
        }

        long min = Long.MAX_VALUE;
        for (DataRow row : items) {
            long x = row.getLong(field);

            if (min > x) {
                min = x;
            }
        }

        return min;
    }
    
    public double minDouble(String field) {
        if (!has(field)) {
            return Double.MAX_VALUE;
        }

        double min = Double.MAX_VALUE;
        for (DataRow row : items) {
            double x = row.getDouble(field);

            if (min > x) {
                min = x;
            }
        }

        return min;
    }
    
    public float minFloat(String field) {
        if (!has(field)) {
            return Float.MAX_VALUE;
        }

        float min = Float.MAX_VALUE;
        for (DataRow row : items) {
            float x = row.getFloat(field);

            if (min > x) {
                min = x;
            }
        }

        return min;
    }
    
    public DataRow pop() {
        if (isEmpty()) {
            return null;
        }

        return items.remove(items.size() - 1);
    }
    
    public DataRow random() {
        if (isEmpty()) {
            return null;
        }

        return items.get(RandomUtil.getInteger(items.size()));
    }
    
    public Collection reverse() {
        Collections.reverse(items);

        return this;
    }
    
    public int search(String field, Object value) {
        if (isEmpty() || !has(field)) {
            return -1;
        }

        String rValue = value.toString();

        for (int index = 0; index < items.size(); index++) {
            DataRow row = get(index);

            if (row.getString(field).equals(rValue)) {
                return index;
            }
        }

        return -1;
    }
    
    public DataRow shift() {
        try {
            return items.remove(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }
    
    public Collection shuffle() {
        Collections.shuffle(items, new SecureRandom());

        return this;
    }
    
    public Collection sort(Comparator<DataRow> comparator) {
        items.sort(comparator);

        return this;
    }
    
    public Collection sortBy(String field) {
        if (!has(field)) {
            return this;
        }

        return sort(Comparator.comparingInt(row -> row.get(field).hashCode()));
    }
    
    public Collection sortByDesc(String field) {
        if (!has(field)) {
            return this;
        }

        return sort((DataRow first, DataRow second) -> second.get(field).hashCode() - first.get(field).hashCode());
    }
    
    public int size() {
        return items.size();
    }
    
    public long sumInt(String field) {
        long sum = 0;

        if (!has(field)) {
            return sum;
        }

        for (DataRow row : items) {
            sum += row.getInt(field);
        }

        return sum;
    }
    
    public Collection take(int amount) {
        Collection collection = new Collection();
        Iterator<DataRow> iterator = items.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            DataRow next = iterator.next();

            if (index++ >= amount) {
                break;
            }

            collection.add(new DataRow(next));
            iterator.remove();
        }

        return collection;
    }
    
    public List<DataRow> where(String field, Object value) {
        if (isEmpty() || !has(field)) {
            return new ArrayList<>();
        }

        String rValue = value.toString();
        List<DataRow> rows = new ArrayList<>();

        items.stream()
                .filter((row) -> (row.getString(field).equals(rValue)))
                .forEach(rows::add);

        return rows;
    }
    
    public List<DataRow> whereLoose(String field, Object value) {
        if (isEmpty() || !has(field)) {
            return new ArrayList<>();
        }

        String rValue = value.toString();
        List<DataRow> rows = new ArrayList<>();

        items.stream()
                .filter((row) -> (row.getString(field).equalsIgnoreCase(rValue)))
                .forEach(rows::add);

        return rows;
    }
    
    @Override
    public String toString() {
        return toJson();
    }

    /**
     * Converts the collection to a JSON string using {@link Gson}.
     *
     * @return the JSON collection string
     */
    public String toJson() {
        return PuddleWorld.gson.toJson(items);
    }
    
    @Nonnull
    @Override
    public Iterator<DataRow> iterator() {
        return new CollectionIterator();
    }

    private void add(DataRow row) {
        this.items.add(new DataRow(row));
    }
    
    private class CollectionIterator implements Iterator<DataRow> {

        private int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor < Collection.this.items.size();
        }

        @Override
        public DataRow next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return Collection.this.items.get(cursor++);
        }
    }
}
