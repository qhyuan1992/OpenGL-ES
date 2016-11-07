package org.obj2openjl.v3.model;

import java.util.Iterator;
import java.util.List;

public class BoundingBox3D {
	
	private float minX;
	private float maxX;
	private float minY;
	public float getMinX() {
		return minX;
	}

	public float getMaxX() {
		return maxX;
	}

	public float getMinY() {
		return minY;
	}

	public float getMaxY() {
		return maxY;
	}

	public float getMinZ() {
		return minZ;
	}

	public float getMaxZ() {
		return maxZ;
	}

	private float maxY;
	private float minZ;
	private float maxZ;
	
	public BoundingBox3D(float minX,
			float maxX,
			float minY,
			float maxY,
			float minZ,
			float maxZ) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.minZ = minZ;
		this.maxZ = maxZ;
	}
	
	public BoundingBox3D(BoundingBox3D boundingBox) {
		this(boundingBox.minX,
				boundingBox.maxX,
				boundingBox.minY,
				boundingBox.maxY,
				boundingBox.minZ,
				boundingBox.maxZ);
	}
	
	public BoundingBox3D(List<Vertex> vertices) {
		Float	minX = null,
				maxX = null,
				minY = null,
				maxY = null,
				minZ = null,
				maxZ = null;
		
		Iterator<Vertex> vertexIterator = vertices.iterator();
		while(vertexIterator.hasNext()) {
			Vertex vertex = vertexIterator.next();
			float x = vertex.getFloatValues()[0];
			float y = vertex.getFloatValues()[1];
			float z = vertex.getFloatValues()[2];
			if(minX == null || minX > x) {
				minX = x;
			}
			if(maxX == null || maxX < x) {
				maxX = x;
			}
			if(minY == null || minY > y) {
				minY = y;
			}
			if(maxY == null || maxY < y) {
				maxY = y;
			}
			if(minZ == null || minZ > z) {
				minZ = z;
			}
			if(maxZ == null || maxZ < z) {
				maxZ = z;
			}
		}
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.minZ = minZ;
		this.maxZ = maxZ;
	}
	
	public float getCenterX() {
		return (this.minX + this.maxX) / 2;
	}
	
	public float getCenterY() {
		return (this.minY + this.maxY) / 2;
	}
	
	public float getCenterZ() {
		return (this.minZ + this.maxZ) / 2;
	}
	
	public void scale(float scaleFactor) {
		this.minX *= scaleFactor;
		this.maxX *= scaleFactor;
		this.minY *= scaleFactor;
		this.maxY *= scaleFactor;
		this.minZ *= scaleFactor;
		this.maxZ *= scaleFactor;
	}
	
	public float[] center() {
		float centerX = this.getCenterX();
		this.minX -= centerX;
		this.maxX -= centerX;
		
		float centerY = this.getCenterY();
		this.minY -= centerY;
		this.maxY -= centerY;
		
		float centerZ = this.getCenterZ();
		this.minZ -= centerZ;
		this.maxZ -= centerZ;
		
		return new float[] {centerX, centerY, centerZ};
	}
	
	public void translate(float distanceX, float distanceY, float distanceZ) {
		this.minX += distanceX;
		this.maxX += distanceX;
		this.minY += distanceY;
		this.maxY += distanceY;
		this.minZ += distanceZ;
		this.maxZ += distanceZ;
	}
	
	public float getLengthX() {
		return this.maxX - this.minX;
	}
	
	public float getLengthY() {
		return this.maxY - this.minY;
	}
	
	public float getLengthZ() {
		return this.maxZ - this.minZ;
	}

}
