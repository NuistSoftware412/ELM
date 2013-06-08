function prepare_tst_all( datafile )
%PREPARE_TST_ALL Summary of this function goes here
%   Detailed explanation goes here
%Urban 1
%Water 2
%Vegetation 3
%Arable land 4
%Wetlands 5
arable_after1=imread('test_samples/arable_after_test1.jpg');
arable_after2=imread('test_samples/arable_after_test2.jpg');
urban_after1=imread('test_samples/urban_after_test1.jpg');
urban_after2=imread('test_samples/urban_after_test2.jpg');
vegetation_after1=imread('test_samples/vegetation_after_test1.jpg');
vegetation_after2=imread('test_samples/vegetation_after_test2.jpg');
water_after1=imread('test_samples/water_after_test1_2.jpg');
water_after2=imread('test_samples/water_after_test2_2.jpg');
wetlands_after1=imread('test_samples/wetlands_after_test1.jpg');
wetlands_after2=imread('test_samples/wetlands_after_test2.jpg');

arable_before1=imread('test_samples/arable_before_test1.jpg');
arable_before2=imread('test_samples/arable_before_test2.jpg');
urban_before1=imread('test_samples/urban_before_test1.jpg');
urban_before2=imread('test_samples/urban_before_test2.jpg');
vegetation_before1=imread('test_samples/vegetation_before_test1.jpg');
vegetation_before2=imread('test_samples/vegetation_before_test2.jpg');
water_before1=imread('test_samples/water_before_test1_2.jpg');
water_before2=imread('test_samples/water_before_test2_2.jpg');
wetlands_before1=imread('test_samples/wetlands_before_test1.jpg');
wetlands_before2=imread('test_samples/wetlands_before_test2.jpg');

fid = fopen(datafile,'w');

for i=1:10
    for j=1:10
        fprintf(fid,'%d %d %d 1\n',urban_after1(i,j,:));
        fprintf(fid,'%d %d %d 1\n',urban_after2(i,j,:));
        
        fprintf(fid,'%d %d %d 2\n',water_after1(i,j,:));
        fprintf(fid,'%d %d %d 2\n',water_after2(i,j,:));
        
        fprintf(fid,'%d %d %d 3\n',vegetation_after1(i,j,:));
        fprintf(fid,'%d %d %d 3\n',vegetation_after2(i,j,:));
        
        fprintf(fid,'%d %d %d 4\n',arable_after1(i,j,:));
        fprintf(fid,'%d %d %d 4\n',arable_after2(i,j,:));
        
        fprintf(fid,'%d %d %d 5\n',wetlands_after1(i,j,:));
        fprintf(fid,'%d %d %d 5\n',wetlands_after2(i,j,:));
    end
end


for i=1:10
    for j=1:10
        fprintf(fid,'%d %d %d 1\n',urban_before1(i,j,:));
        fprintf(fid,'%d %d %d 1\n',urban_before2(i,j,:));
        
        fprintf(fid,'%d %d %d 2\n',water_before1(i,j,:));
        fprintf(fid,'%d %d %d 2\n',water_before2(i,j,:));
        
        fprintf(fid,'%d %d %d 3\n',vegetation_before1(i,j,:));
        fprintf(fid,'%d %d %d 3\n',vegetation_before2(i,j,:));
        
        fprintf(fid,'%d %d %d 4\n',arable_before1(i,j,:));
        fprintf(fid,'%d %d %d 4\n',arable_before2(i,j,:));
        
        fprintf(fid,'%d %d %d 5\n',wetlands_before1(i,j,:));
        fprintf(fid,'%d %d %d 5\n',wetlands_before2(i,j,:));
    end
end

fclose(fid);

end

