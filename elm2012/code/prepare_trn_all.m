function prepare_trn_all(datafile)
%PREPARE Summary of this function goes here
%   Detailed explanation goes here
% Write all the training jpg picture to data file
%Urban 1
%Water 2
%Vegetation 3
%Arable land 4
%Wetlands 5
arable_after=imread('train_samples/arable_after_train.jpg');
urban_after=imread('train_samples/urban_after_train.jpg');
vegetation_after=imread('train_samples/vegetation_after_train.jpg');
water_after=imread('train_samples/water_after_train2.jpg');
wetlands_after=imread('train_samples/wetlands_after_train.jpg');


arable_before=imread('train_samples/arable_before_train.jpg');
urban_before=imread('train_samples/urban_before_train.jpg');
vegetation_before=imread('train_samples/vegetation_before_train.jpg');
water_before=imread('train_samples/water_before_train2.jpg');
wetlands_before=imread('train_samples/wetlands_before_train.jpg');

fid = fopen(datafile,'w')

for i=1:20
    for j=1:20
        fprintf(fid,'%d %d %d 1\n',urban_after(i,j,:));
        fprintf(fid,'%d %d %d 2\n',water_after(i,j,:));
        fprintf(fid,'%d %d %d 3\n',vegetation_after(i,j,:));
        fprintf(fid,'%d %d %d 4\n',arable_after(i,j,:));
        fprintf(fid,'%d %d %d 5\n',wetlands_after(i,j,:));
    end
end

for i=1:20
    for j=1:20
        fprintf(fid,'%d %d %d 1\n',urban_before(i,j,:));
        fprintf(fid,'%d %d %d 2\n',water_before(i,j,:));
        fprintf(fid,'%d %d %d 3\n',vegetation_before(i,j,:));
        fprintf(fid,'%d %d %d 4\n',arable_before(i,j,:));
        fprintf(fid,'%d %d %d 5\n',wetlands_before(i,j,:));
    end
end


fclose(fid);

end

