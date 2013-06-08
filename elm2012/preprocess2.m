function preprocess2(infile,outfile)
%PREPROCESS2 Summary of this function goes here
%   Detailed explanation goes here
% This function is to preprocess the image data
in=load(infile);

%rand_sequence=randperm(size(in,1));
%temp_dataset=in;
%in=temp_dataset(rand_sequence, :);
for i=1:size(in,2)
    in(:,i)=(in(:,i)-min(in(:,i)))/(max(in(:,i))-min(in(:,i)))*2-1;
end

out=fopen(outfile,'w');
for i=1:size(in,1)
        for j=1:size(in,2)
            fprintf(out,'%2.8f ', in(i,j));    %   for ELM
        end
        fprintf(out,'\n');
end
fclose(out);


end

