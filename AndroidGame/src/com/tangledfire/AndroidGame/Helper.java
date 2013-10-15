package com.tangledfire.AndroidGame;

import java.awt.Point;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
/**
 * @author Nikhil Khanna
 * Contains convenience methods (camera correct, line intersection, etc.)
 */
public class Helper {
	public static final float epsilon = .001f;
	public static float simplePointerY()
	{
		return AndroidGame.h-Gdx.input.getY();
	}
	public static boolean isEpsilonEquals(float first, float second)
	{
		return(Math.abs(first-second)<epsilon);
	}
	public static float PointerY()//gets pointer position(adjusted for camera)
	{
		return ((AndroidGame.h-Gdx.input.getY())*AndroidGame.camera.zoom+AndroidGame.camera.position.y-AndroidGame.h/2*AndroidGame.camera.zoom);
	}
	public static float PointerX()//gets pointer X (adjusted for camera)
	{
		return (Gdx.input.getX()*AndroidGame.camera.zoom+AndroidGame.camera.position.x-AndroidGame.w/2*AndroidGame.camera.zoom);
	}
	public static float PointerYNoZoom()
	{
		return ((AndroidGame.h-Gdx.input.getY())+AndroidGame.camera.position.y-AndroidGame.h/2);
	}
	public static float PointerXNoZoom()
	{
		return (Gdx.input.getX()+AndroidGame.camera.position.x-AndroidGame.w/2);

	}
	public static float cameraCorrectX(float x)
	{
		return (x+AndroidGame.camera.position.x-AndroidGame.w/2);
	}
	public static float cameraCorrectY(float y)
	{
		return (y+AndroidGame.camera.position.y-AndroidGame.h/2);
	}
	public static boolean intersect(Rectangle rectangle1, Rectangle rectangle2, Rectangle intersection) {
	    if (rectangle1.overlaps(rectangle2)) {
	        intersection.x = Math.max(rectangle1.x, rectangle2.x);
	        intersection.width = Math.min(rectangle1.x + rectangle1.width, rectangle2.x + rectangle2.width) - intersection.x;
	        intersection.y = Math.max(rectangle1.y, rectangle2.y);
	        intersection.height = Math.min(rectangle1.y + rectangle1.height, rectangle2.y + rectangle2.height) - intersection.y;
	        return true;
	    }
	    return false;
	}
	public static boolean linesIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
	      // Return false if either of the lines have zero length
	      if (x1 == x2 && y1 == y2 ||
	            x3 == x4 && y3 == y4){
	         return false;
	      }
	      // Fastest method, based on Franklin Antonio's "Faster Line Segment Intersection" topic "in Graphics Gems III" book (http://www.graphicsgems.org/)
	      double ax = x2-x1;
	      double ay = y2-y1;
	      double bx = x3-x4;
	      double by = y3-y4;
	      double cx = x1-x3;
	      double cy = y1-y3;

	      double alphaNumerator = by*cx - bx*cy;
	      double commonDenominator = ay*bx - ax*by;
	      if (commonDenominator > 0){
	         if (alphaNumerator < 0 || alphaNumerator > commonDenominator){
	            return false;
	         }
	      }else if (commonDenominator < 0){
	         if (alphaNumerator > 0 || alphaNumerator < commonDenominator){
	            return false;
	         }
	      }
	      double betaNumerator = ax*cy - ay*cx;
	      if (commonDenominator > 0){
	         if (betaNumerator < 0 || betaNumerator > commonDenominator){
	            return false;
	         }
	      }else if (commonDenominator < 0){
	         if (betaNumerator > 0 || betaNumerator < commonDenominator){
	            return false;
	         }
	      }
	      if (commonDenominator == 0){
	         // This code wasn't in Franklin Antonio's method. It was added by Keith Woodward.
	         // The lines are parallel.
	         // Check if they're collinear.
	         double y3LessY1 = y3-y1;
	         double collinearityTestForP3 = x1*(y2-y3) + x2*(y3LessY1) + x3*(y1-y2);   // see http://mathworld.wolfram.com/Collinear.html
	         // If p3 is collinear with p1 and p2 then p4 will also be collinear, since p1-p2 is parallel with p3-p4
	         if (collinearityTestForP3 == 0){
	            // The lines are collinear. Now check if they overlap.
	            if (x1 >= x3 && x1 <= x4 || x1 <= x3 && x1 >= x4 ||
	                  x2 >= x3 && x2 <= x4 || x2 <= x3 && x2 >= x4 ||
	                  x3 >= x1 && x3 <= x2 || x3 <= x1 && x3 >= x2){
	               if (y1 >= y3 && y1 <= y4 || y1 <= y3 && y1 >= y4 ||
	                     y2 >= y3 && y2 <= y4 || y2 <= y3 && y2 >= y4 ||
	                     y3 >= y1 && y3 <= y2 || y3 <= y1 && y3 >= y2){
	                  return true;
	               }
	            }
	         }
	         return false;
	      }
	      return true;
	   }
	public static Vector2 getLineLineIntersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float det1And2 = det(x1, y1, x2, y2);
		float det3And4 = det(x3, y3, x4, y4);
		float x1LessX2 = x1 - x2;
	      float y1LessY2 = y1 - y2;
	      float x3LessX4 = x3 - x4;
	      float y3LessY4 = y3 - y4;
	      float det1Less2And3Less4 = det(x1LessX2, y1LessY2, x3LessX4, y3LessY4);
	      if (det1Less2And3Less4 == 0){
	         // the denominator is zero so the lines are parallel and there's either no solution (or multiple solutions if the lines overlap) so return null.
	         return null;
	      }
	      float x = (det(det1And2, x1LessX2,
	            det3And4, x3LessX4) /
	            det1Less2And3Less4);
	      float y = (det(det1And2, y1LessY2,
	            det3And4, y3LessY4) /
	            det1Less2And3Less4);
	      return new Vector2(x, y);
	   }
	   protected static float det(float a, float b, float c, float d) {
	      return a * d - b * c;
	   }
}
