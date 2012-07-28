package br.usp.ime.messagereader;

import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MessageReaderTest {

	@Test
	public void testReadMessages () {
		MessageReader reader = new MessageReader ("input/messageinput1.in");
		List<Message> m = reader.readMessages();
		assertEquals(m.size(), 3);
		assertEquals(m.get(0).getCreationTime(), 2);
		assertEquals(m.get(0).getOrigin(), "3");
		assertEquals(m.get(0).getDestination(), "10");
		assertEquals(m.get(1).getCreationTime(), 5);
		assertEquals(m.get(1).getOrigin(), "8");
		assertEquals(m.get(1).getDestination(), "15");
		assertEquals(m.get(2).getCreationTime(), 32);
		assertEquals(m.get(2).getOrigin(), "24");
		assertEquals(m.get(2).getDestination(), "87");
	}
}
