//Code by: I.Chatzivasileiadou
package milestone2_FlightADT;

public class ResNode
{
	private Reservation reservationItem;
	private ResNode next;

	public ResNode()
	{
		next = null;
	}

	public ResNode(Reservation newReservationItem)
	{
		reservationItem = newReservationItem;
		next = null;
	}

	public ResNode(Reservation newReservationItem, ResNode nextNode)
	{
		reservationItem = newReservationItem;
		next = nextNode;
	}

	public void setReservationItem(Reservation newReservationItem)
	{
		reservationItem = newReservationItem;
	}

	public Reservation getReservationItem()
	{
		return reservationItem;
	}

	public void setNext(ResNode nextNode)
	{
		next = nextNode;
	}

	public ResNode getNext()
	{
		return next;
	}
};