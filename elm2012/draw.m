function draw( group_file,x,y)
%DRAW Summary of this function goes here
%   Detailed explanation goes here
%1 Urban, Whrite, RGB: 1 1 1
%2 Water, Blue, RGB: 0 0 1
%3 Vegetation, Green, RGB: 0 1 0
%4 Arable Land, Yellow, RGB: 1 1 0
%5 Wetlands, Red, RGB: 1 0 1
group=load(group_file);
d=zeros(x,y,3);
k=1;
for i=1:x
    for j=1:y
        switch group(k)
            case 1
                d(i,j,1)=1;
                d(i,j,2)=1;
                d(i,j,3)=1;
            case 2
                d(i,j,1)=0;
                d(i,j,2)=0;
                d(i,j,3)=1;
            case 3
                d(i,j,1)=0;
                d(i,j,2)=1;
                d(i,j,3)=0;
            case 4
                d(i,j,1)=1;
                d(i,j,2)=1;
                d(i,j,3)=0;
            case 5
                d(i,j,1)=1;
                d(i,j,2)=0;
                d(i,j,3)=1;
        end
        k=k+1;
    end
end
imshow(d);
end

