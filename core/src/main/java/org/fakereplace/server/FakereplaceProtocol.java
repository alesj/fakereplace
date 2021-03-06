package org.fakereplace.server;

import org.fakereplace.Agent;
import org.fakereplace.boot.DefaultEnvironment;
import org.fakereplace.replacement.AddedClass;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.UnmodifiableClassException;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of the fakereplace client server protocol.
 * <p/>
 * The basic protocol is as follows:
 * <p/>
 * Client -
 * Magic no 0xCAFEDEAF
 * no classes (int)
 * class data (1 per class)
 * class name length (int)
 * class name
 * timestamp (long)
 * <p/>
 * Server -
 * no classes (int)
 * class data (1 per class)
 * class name length
 * class name
 * <p/>
 * Client -
 * no classes (int)
 * class data
 * class name length
 * class name
 * class bytes length
 * class bytes
 *
 * @author Stuart Douglas
 */
public class FakereplaceProtocol {

    public static void run(Socket socket) {
        try {
            final DataInputStream input = new DataInputStream(socket.getInputStream());
            final DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            final Map<String, Long> classes = new HashMap<String, Long>();
            final Map<String, Long> resources = new HashMap<String, Long>();
            int magic = input.readInt();
            if (magic != 0xCAFEDEAF) {
                System.err.println("Fakereplace server error, wrong magic number");
                return;
            }
            int nameLength = input.readInt();
            char[] nameBuffer = new char[nameLength];
            for (int pos = 0; pos < nameLength; ++pos) {
                nameBuffer[pos] = input.readChar();
            }
            final String archiveName = new String(nameBuffer);

            readAvailable(input, classes);
            readAvailable(input, resources);


            final Set<Class> classesToReplace = DefaultEnvironment.getEnvironment().getUpdatedClasses(archiveName, classes);
            final Map<String, Class> classMap = new HashMap<String, Class>();
            output.writeInt(classesToReplace.size());
            for (Class clazz : classesToReplace) {
                final String cname = clazz.getName();
                output.writeInt(cname.length());
                for (int i = 0; i < cname.length(); ++i) {
                    output.writeChar(cname.charAt(i));
                }
                classMap.put(cname, clazz);
            }
            final Set<String> resourcesToReplace = DefaultEnvironment.getEnvironment().getUpdatedResources(archiveName, resources);
            output.writeInt(resourcesToReplace.size());
            for (String cname : resourcesToReplace) {
                output.writeInt(cname.length());
                for (int i = 0; i < cname.length(); ++i) {
                    output.writeChar(cname.charAt(i));
                }
            }

            output.flush();

            final Set<ClassDefinition> classDefinitions = new HashSet<ClassDefinition>();
            final Set<Class<?>> replacedClasses = new HashSet<Class<?>>();
            int noClasses = input.readInt();
            for (int i = 0; i < noClasses; ++i) {
                int length = input.readInt();
                nameBuffer = new char[length];
                for (int pos = 0; pos < length; ++pos) {
                    nameBuffer[pos] = input.readChar();
                }
                final String className = new String(nameBuffer);
                length = input.readInt();
                byte[] buffer = new byte[length];
                for (int j = 0; j < length; ++j) {
                    buffer[j] = (byte) input.read();
                }
                classDefinitions.add(new ClassDefinition(classMap.get(className), buffer));
                replacedClasses.add(classMap.get(className));
            }

            final Map<String, byte[]> replacedResources = new HashMap<String, byte[]>();

            int noResources = input.readInt();
            for (int i = 0; i < noResources; ++i) {
                int length = input.readInt();
                nameBuffer = new char[length];
                for (int pos = 0; pos < length; ++pos) {
                    nameBuffer[pos] = input.readChar();
                }
                final String resourceName = new String(nameBuffer);
                length = input.readInt();
                byte[] buffer = new byte[length];
                for (int j = 0; j < length; ++j) {
                    buffer[j] = (byte) input.read();
                }
                replacedResources.put(resourceName, buffer);
            }

            Agent.redefine(classDefinitions.toArray(new ClassDefinition[classDefinitions.size()]), new AddedClass[0]);
            DefaultEnvironment.getEnvironment().updateResource(archiveName, replacedResources);

            
            DefaultEnvironment.getEnvironment().afterReplacement(replacedClasses, archiveName);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (UnmodifiableClassException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void readAvailable(final DataInputStream input, final Map<String, Long> resources) throws IOException {
        char[] nameBuffer;
        int noResources = input.readInt();
        for (int i = 0; i < noResources; ++i) {
            int length = input.readInt();
            nameBuffer = new char[length];
            for (int pos = 0; pos < length; ++pos) {
                nameBuffer[pos] = input.readChar();
            }
            final String resourceName = new String(nameBuffer);
            long ts = input.readLong();
            resources.put(resourceName, ts);
        }
    }

}
