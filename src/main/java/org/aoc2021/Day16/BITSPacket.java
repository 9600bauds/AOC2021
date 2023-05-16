package org.aoc2021.Day16;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BITSPacket {
    static final int PACKET_VERSION_LEN = 3;

    static final int PACKET_TYPEID_LEN = 3;

    static final int TYPEID_SUM = 0;
    static final int TYPEID_PRODUCT = 1;
    static final int TYPEID_MIN = 2;
    static final int TYPEID_MAX = 3;
    static final int TYPEID_LITERAL_VALUE = 4;
    static final int TYPEID_GREATERTHAN = 5;
    static final int TYPEID_LESSTHAN = 6;
    static final int TYPEID_EQUALTO = 7;

    static final int LITERAL_VALUE_BYTE_LEN = 4;
    static final int OPERATOR_SUBPACKET_LEN0 = 15;
    static final int OPERATOR_SUBPACKET_LEN1 = 11;

    String binaryString; //Just for debugging really
    Long value; //The final value of this packet
    int version;
    int typeID;
    List<BITSPacket> subpackets = new ArrayList<>();

    public BITSPacket(BITSScanner scanner) {
            String packetVersionString = scanner.getBits(PACKET_VERSION_LEN);
            version = Integer.parseInt(packetVersionString, 2);

            String packetTypeIDString = scanner.getBits(PACKET_TYPEID_LEN);
            typeID = Integer.parseInt(packetTypeIDString, 2);

            //System.out.println("Version " + packetVersion + " Type " + packetTypeID);

            if(typeID == TYPEID_LITERAL_VALUE) {
                boolean done = false;
                StringBuilder literalValueStr = new StringBuilder();
                while (!done) {
                    String prefix = scanner.getBits(1);
                    if (prefix.equals("0")) {
                        done = true;
                    }
                    literalValueStr.append(scanner.getBits(LITERAL_VALUE_BYTE_LEN));
                }
                value = Long.parseLong(literalValueStr.toString(), 2);
                //System.out.println("Created a literal value packet with value " + value + ".");
            } else {
                //This is an operator packet!
                String lengthPrefix = scanner.getBits(1);
                if(lengthPrefix.equals("0")){
                    //the next 15 bits are a number that represents the total length in bits of the sub-packets contained by this packet.
                    String totalLengthString = scanner.getBits(OPERATOR_SUBPACKET_LEN0);
                    int totalLength = Integer.parseInt(totalLengthString, 2);

                    String subpacketsString = scanner.getBits(totalLength);
                    BITSScanner subScanner = new BITSScanner(subpacketsString);
                    while(!subScanner.Done()){
                        BITSPacket child = new BITSPacket(subScanner);
                        subpackets.add(child);
                    }
                }
                else if(lengthPrefix.equals("1")){
                    //the next 11 bits are a number that represents the number of sub-packets immediately contained by this packet.
                    String subPacketCountString = scanner.getBits(OPERATOR_SUBPACKET_LEN1);
                    int subPacketCount = Integer.parseInt(subPacketCountString, 2);
                    for(int packetIndex = 0; packetIndex < subPacketCount; packetIndex++){
                        BITSPacket child = new BITSPacket(scanner);
                        subpackets.add(child);
                    }
                }
                else{
                    throw new RuntimeException("Invalid length prefix! " + lengthPrefix);
                }
                //System.out.println("Created an operator packet with " + subpackets.size() + " subpackets.");
            }
        }

        public int RecursivelyGetPacketVersionSum(){
            int out = version;
            for(BITSPacket child : subpackets){
                out += child.RecursivelyGetPacketVersionSum();
            }
            return out;
        }

        public void RecursivelyCalculateValue(){
            if(value != null){
                return;
            }
            for(BITSPacket child : subpackets){
                child.RecursivelyCalculateValue();
            }
            switch(typeID){
                case TYPEID_SUM:
                    value = 0L;
                    for(BITSPacket child : subpackets){
                        value += child.value;
                    }
                    break;
                case TYPEID_PRODUCT:
                    value = 1L; //identity value for multiplication
                    for(BITSPacket child : subpackets){
                        value *= child.value;
                    }
                    break;
                case TYPEID_MIN:
                    value = Long.MAX_VALUE;
                    for(BITSPacket child : subpackets){
                        if(child.value < value){
                            value = child.value;
                        }
                    }
                    break;
                case TYPEID_MAX:
                    value = Long.MIN_VALUE;
                    for(BITSPacket child : subpackets){
                        if(child.value > value){
                            value = child.value;
                        }
                    }
                    break;
                case TYPEID_LITERAL_VALUE:
                    throw new RuntimeException("Literal value packet somehow doesn't have a value already!");
                case TYPEID_GREATERTHAN:
                    if(subpackets.get(0).value > subpackets.get(1).value){
                        value = 1L;
                    }
                    else{
                        value = 0L;
                    }
                    break;
                case TYPEID_LESSTHAN:
                    if(subpackets.get(0).value < subpackets.get(1).value){
                        value = 1L;
                    }
                    else{
                        value = 0L;
                    }
                    break;
                case TYPEID_EQUALTO:
                    if(Objects.equals(subpackets.get(0).value, subpackets.get(1).value)){
                        value = 1L;
                    }
                    else{
                        value = 0L;
                    }
                    break;
                default:
                    throw new RuntimeException("Invalid typeID!");
            }
        }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        switch (typeID) {
            case TYPEID_SUM:
                sb.append("SUM(").append(subpackets.stream().map(Object::toString).collect(Collectors.joining(","))).append(")");
                break;
            case TYPEID_PRODUCT:
                sb.append("PRODUCT(").append(subpackets.stream().map(Object::toString).collect(Collectors.joining(","))).append(")");
                break;
            case TYPEID_MIN:
                sb.append("MIN(").append(subpackets.stream().map(Object::toString).collect(Collectors.joining(","))).append(")");
                break;
            case TYPEID_MAX:
                sb.append("MAX(").append(subpackets.stream().map(Object::toString).collect(Collectors.joining(","))).append(")");
                break;
            case TYPEID_LITERAL_VALUE:
                sb.append(value);
                break;
            case TYPEID_GREATERTHAN:
                sb.append("GREATERTHAN(").append(subpackets.stream().map(Object::toString).collect(Collectors.joining(","))).append(")");
                break;
            case TYPEID_LESSTHAN:
                sb.append("LESSTHAN(").append(subpackets.stream().map(Object::toString).collect(Collectors.joining(","))).append(")");
                break;
            case TYPEID_EQUALTO:
                sb.append("EQUAL(").append(subpackets.stream().map(Object::toString).collect(Collectors.joining(","))).append(")");
                break;
        }
        return sb.toString();
    }
}





