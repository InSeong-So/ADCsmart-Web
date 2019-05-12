package kr.openbase.adcsmart.service.utility;

public class OBIpTree {
    private OBNumberNode root = new OBNumberNode("");

    public void add(String ip) {
        String[] ipNumbers = ip.split("\\.");
        OBNumberNode node = root;
        for (String number : ipNumbers)
            node = node.createOrGetChildNumber(number);
    }

    public boolean containsIp(String ip) {
        String[] ipNumbers = ip.split("\\.");
        OBNumberNode node = root;
        for (String number : ipNumbers) {
            node = node.findMatchingChild(number);
            if (node == null)
                return false;
            if (node.isAllAccept())
                return true;
        }
        return true;
    }
}
