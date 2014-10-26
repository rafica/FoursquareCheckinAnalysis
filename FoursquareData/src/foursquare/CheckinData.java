package foursquare;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;





public class CheckinData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File file = new File("checkins");
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);

			byte[] data = new byte[(int)file.length()];
			fis.read(data);
			fis.close();
			String s = new String(data, "UTF-8");

			Object obj = JSONValue.parse(s);
			JSONObject json = (JSONObject)obj;

			json = (JSONObject)json.get("response");
			JSONArray array = (JSONArray)(((JSONObject)json.get("checkins")).get("items"));
			int num = array.size();
			Map<String, Integer> place = new HashMap<String, Integer>();
			Map<String, Integer> category = new HashMap<String, Integer>();

			FileWriter writer = new FileWriter("checkindata.csv");
			writer.append("Time");
			writer.append(",");
			writer.append("Place");
			writer.append('\n');

			FileWriter writer1 = new FileWriter("categorydata.csv");
			writer1.append("Time");
			writer1.append(",");
			writer1.append("Category");
			writer1.append('\n');
			for(int i=0; i< num;i++) {
				JSONObject checkin = (JSONObject)array.get(i);
				String time = checkin.get("createdAt").toString();
				JSONObject venueObj = (JSONObject)checkin.get("venue");
				String name = venueObj.get("name").toString();
				JSONArray catArr = (JSONArray)venueObj.get("categories");
				for(int j =0; j<catArr.size(); j++) {
					String categoryName = ((JSONObject)catArr.get(j)).get("name").toString();
					if(category.containsKey(categoryName)){
						int count = category.get(categoryName);
						category.put(categoryName, count+1);
					}
					else{
						category.put(categoryName, 1);
					}
					writer1.append(time);
					writer1.append(',');
					writer1.append(categoryName);
					writer1.append('\n');
				}
				if(place.containsKey(name)){
					int count = place.get(name);
					place.put(name, count+1);
				}
				else{
					place.put(name, 1);
				}
				writer.append(time);
				writer.append(',');
				writer.append(name);
				writer.append('\n');



			}
			writer.flush();
			writer.close();
			writer1.flush();
			writer1.close();
//			Iterator<Entry<String, Integer>> it = place.entrySet().iterator();
//			while (it.hasNext()) {
//				Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>)it.next();
//				if(pairs.getValue()>=2)
//					System.out.println(pairs.getKey() + " = " + pairs.getValue());
//				it.remove(); // avoids a ConcurrentModificationException
//			}
			Iterator<Entry<String, Integer>> it = category.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>)it.next();
				if(pairs.getValue()>=2)
					System.out.println(pairs.getKey() + ":" + pairs.getValue());
				it.remove(); // avoids a ConcurrentModificationException
			}
			//System.out.println(place.toString());
			//System.out.println(category.toString());
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
