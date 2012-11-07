package org.uiautomation.ios.webInspector.DOM;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Node {

  public String getDocumentURL() {
    return documentURL;
  }

  private final JSONObject raw;
  private int nodeId;
  private int childCount;
  private List<Node> children = new ArrayList<Node>();
  private String nodeName;
  private Node contentDocument;
  private String documentURL;
  private int nodeType;


  public Node(JSONObject o) throws JSONException {
    this.raw = o;

    this.nodeId = o.getInt("nodeId");
    this.nodeName = o.optString("nodeName");
    this.childCount = o.optInt("childCount", 0);

    this.documentURL = o.optString("documentURL");
    this.nodeType = o.optInt("nodeType", 0);
    
    JSONObject document = o.optJSONObject("contentDocument");
    if (document!=null){
      contentDocument = new Node(document);
    }

    JSONArray children = o.optJSONArray("children");
    if (children != null) {
      for (int i = 0; i < children.length(); i++) {
        JSONObject child = children.getJSONObject(i);
        this.children.add(new Node(child));
      }
    }
  }


  public Node getContentDocument() {
    return contentDocument;
  }


  public int getId() {
    return nodeId;
  }


  public void setId(int id) {
    this.nodeId = id;
  }


  public int getChildCount() {
    return childCount;
  }


  public void setChildCount(int childCount) {
    this.childCount = childCount;
  }


  public List<Node> getChildren() {
    return children;
  }


  public void setChildren(List<Node> children) {
    this.children = children;
  }


  public String getNodeName() {
    return nodeName;
  }


  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  public Node getBody() {
    Node res = null;
    for (Node c : getChildren()) {
      if ("BODY".equals(c.getNodeName())) {
        res = c;
        return res;
      }
      res = c.getBody();
      if (res != null) {
        return res;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    try {
      return raw.toString(2);
    } catch (JSONException e) {
      return "ERROR parsing the raw node.";
    }
  }
}
