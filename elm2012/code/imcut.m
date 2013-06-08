function imcut( x_min,x_max,y_min,y_max,pic,filename )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
i=imread(pic);
o=i(y_min:y_max,x_min:x_max,:);
imwrite(o,filename);
end

